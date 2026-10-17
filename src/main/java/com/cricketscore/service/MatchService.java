package com.cricketscore.service;

import com.cricketscore.dto.*;
import com.cricketscore.model.*;
import com.cricketscore.model.enums.MatchStatus;
import com.cricketscore.model.enums.TossDecision;
import com.cricketscore.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final InningsRepository inningsRepository;
    private final BattingScorecardRepository battingScorecardRepository;
    private final BowlingScorecardRepository bowlingScorecardRepository;
    private final BallEventRepository ballEventRepository;

    public MatchService(MatchRepository matchRepository,
                        TeamRepository teamRepository,
                        PlayerRepository playerRepository,
                        InningsRepository inningsRepository,
                        BattingScorecardRepository battingScorecardRepository,
                        BowlingScorecardRepository bowlingScorecardRepository,
                        BallEventRepository ballEventRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.inningsRepository = inningsRepository;
        this.battingScorecardRepository = battingScorecardRepository;
        this.bowlingScorecardRepository = bowlingScorecardRepository;
        this.ballEventRepository = ballEventRepository;
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAllByOrderByMatchDateDesc();
    }

    public List<Match> getMatchesByStatus(MatchStatus status) {
        return matchRepository.findByStatusOrderByMatchDateDesc(status);
    }

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public LiveMatchUpdateDto getLiveUpdate(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        LiveMatchUpdateDto dto = new LiveMatchUpdateDto();
        dto.setMatchId(match.getId());
        dto.setTitle(match.getTitle());
        dto.setStatus(match.getStatus().name());
        dto.setMatchType(match.getMatchType().name());
        dto.setCurrentInningsNumber(match.getCurrentInningsNumber());
        dto.setTotalOversLimit(match.getTotalOversLimit());
        dto.setSimulating(match.isSimulating());
        dto.setWinMargin(match.getWinMargin());
        if (match.getWinner() != null) {
            dto.setWinnerTeamName(match.getWinner().getName());
        }

        Optional<Innings> currentInningsOpt = inningsRepository
                .findByMatchIdAndInningsNumber(match.getId(), match.getCurrentInningsNumber());

        if (currentInningsOpt.isPresent()) {
            Innings innings = currentInningsOpt.get();
            dto.setTotalRuns(innings.getTotalRuns());
            dto.setTotalWickets(innings.getTotalWickets());
            dto.setLegalBalls(innings.getLegalBalls());
            dto.setOversFormatted(innings.getOversFormatted());
            dto.setCurrentScore(innings.getTotalRuns() + "/" + innings.getTotalWickets());
            dto.setCurrentRunRate(innings.getCurrentRunRate());
            dto.setRequiredRunRate(innings.getRequiredRunRate(match.getTotalOversLimit()));
            dto.setProjectedScore(innings.getProjectedScore(match.getTotalOversLimit()));
            dto.setTarget(innings.getTarget());

            if (innings.getBattingTeam() != null) {
                dto.setBattingTeamName(innings.getBattingTeam().getName());
                dto.setBattingTeamShort(innings.getBattingTeam().getShortName());
                dto.setBattingTeamFlag(innings.getBattingTeam().getFlagEmoji());
                dto.setBattingTeamColor(innings.getBattingTeam().getPrimaryColor());
            }

            if (innings.getBowlingTeam() != null) {
                dto.setBowlingTeamName(innings.getBowlingTeam().getName());
                dto.setBowlingTeamShort(innings.getBowlingTeam().getShortName());
                dto.setBowlingTeamFlag(innings.getBowlingTeam().getFlagEmoji());
                dto.setBowlingTeamColor(innings.getBowlingTeam().getPrimaryColor());
            }

            // Striker details
            if (innings.getStrikerPlayerId() != null) {
                battingScorecardRepository.findByInningsIdAndPlayerId(innings.getId(), innings.getStrikerPlayerId())
                        .ifPresent(sc -> {
                            dto.setStrikerId(sc.getPlayer().getId());
                            dto.setStrikerName(sc.getPlayer().getName());
                            dto.setStrikerRuns(sc.getRuns());
                            dto.setStrikerBalls(sc.getBallsFaced());
                            dto.setStrikerFours(sc.getFours());
                            dto.setStrikerSixes(sc.getSixes());
                            dto.setStrikerStrikeRate(sc.getStrikeRate());
                        });
            }

            // Non-Striker details
            if (innings.getNonStrikerPlayerId() != null) {
                battingScorecardRepository.findByInningsIdAndPlayerId(innings.getId(), innings.getNonStrikerPlayerId())
                        .ifPresent(sc -> {
                            dto.setNonStrikerId(sc.getPlayer().getId());
                            dto.setNonStrikerName(sc.getPlayer().getName());
                            dto.setNonStrikerRuns(sc.getRuns());
                            dto.setNonStrikerBalls(sc.getBallsFaced());
                            dto.setNonStrikerStrikeRate(sc.getStrikeRate());
                        });
            }

            // Current Bowler details
            if (innings.getCurrentBowlerPlayerId() != null) {
                bowlingScorecardRepository.findByInningsIdAndPlayerId(innings.getId(), innings.getCurrentBowlerPlayerId())
                        .ifPresent(bc -> {
                            dto.setBowlerId(bc.getPlayer().getId());
                            dto.setBowlerName(bc.getPlayer().getName());
                            dto.setBowlerOvers(bc.getOversFormatted());
                            dto.setBowlerRunsConceded(bc.getRunsConceded());
                            dto.setBowlerWickets(bc.getWicketsTaken());
                            dto.setBowlerEconomy(bc.getEconomyRate());
                        });
            }

            // Recent balls in current over
            List<BallEvent> recentBalls = ballEventRepository.findTop12ByInningsIdOrderByIdDesc(innings.getId());
            List<String> ballDisplays = new ArrayList<>();
            for (BallEvent be : recentBalls) {
                ballDisplays.add(be.getBallDisplay());
            }
            // Reverse so oldest of the recent comes first (left-to-right timeline)
            Collections.reverse(ballDisplays);
            dto.setRecentBalls(ballDisplays);

            if (!recentBalls.isEmpty()) {
                BallEvent latest = recentBalls.get(0);
                dto.setLatestCommentary(latest.getCommentaryText());
                dto.setLatestBallDisplay(latest.getBallDisplay());
            }
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public ScorecardResponseDto getMatchScorecard(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        ScorecardResponseDto response = new ScorecardResponseDto();
        response.setMatch(match);

        List<Innings> inningsList = inningsRepository.findByMatchIdOrderByInningsNumberAsc(matchId);
        for (Innings inn : inningsList) {
            ScorecardResponseDto.InningsScorecardDto dto = new ScorecardResponseDto.InningsScorecardDto();
            dto.setInningsId(inn.getId());
            dto.setInningsNumber(inn.getInningsNumber());
            if (inn.getBattingTeam() != null) {
                dto.setTeamName(inn.getBattingTeam().getName());
                dto.setTeamShort(inn.getBattingTeam().getShortName());
                dto.setTeamFlag(inn.getBattingTeam().getFlagEmoji());
            }
            dto.setTotalRuns(inn.getTotalRuns());
            dto.setTotalWickets(inn.getTotalWickets());
            dto.setOversFormatted(inn.getOversFormatted());
            dto.setRunRate(inn.getCurrentRunRate());
            dto.setTarget(inn.getTarget());
            dto.setExtrasTotal(inn.getExtrasTotal());
            dto.setWides(inn.getWides());
            dto.setNoBalls(inn.getNoBalls());
            dto.setByes(inn.getByes());
            dto.setLegByes(inn.getLegByes());

            dto.setBatsmen(battingScorecardRepository.findByInningsIdOrderByBattingPositionAsc(inn.getId()));
            dto.setBowlers(bowlingScorecardRepository.findByInningsId(inn.getId()));

            if (inn.getInningsNumber() == 1) {
                response.setInnings1(dto);
            } else if (inn.getInningsNumber() == 2) {
                response.setInnings2(dto);
            }
        }

        return response;
    }

    @Transactional(readOnly = true)
    public MatchSummaryDto getMatchSummary(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        MatchSummaryDto summary = new MatchSummaryDto();
        summary.setMatchId(match.getId());
        summary.setTitle(match.getTitle());
        summary.setStatus(match.getStatus().name());
        summary.setResultText(match.getWinMargin());
        summary.setManOfTheMatch(match.getManOfTheMatch());

        List<Innings> inningsList = inningsRepository.findByMatchIdOrderByInningsNumberAsc(matchId);
        if (!inningsList.isEmpty()) {
            Innings inn1 = inningsList.get(0);
            summary.setTeam1Name(inn1.getBattingTeam().getName());
            summary.setTeam1Score(inn1.getTotalRuns() + "/" + inn1.getTotalWickets());
            summary.setTeam1Overs(inn1.getOversFormatted());

            populateTopPerformers(inn1.getId(), summary, 1);
            summary.setInnings1Progression(calculateOverProgression(inn1.getId()));
        }

        if (inningsList.size() > 1) {
            Innings inn2 = inningsList.get(1);
            summary.setTeam2Name(inn2.getBattingTeam().getName());
            summary.setTeam2Score(inn2.getTotalRuns() + "/" + inn2.getTotalWickets());
            summary.setTeam2Overs(inn2.getOversFormatted());

            populateTopPerformers(inn2.getId(), summary, 2);
            summary.setInnings2Progression(calculateOverProgression(inn2.getId()));
        }

        return summary;
    }

    private void populateTopPerformers(Long inningsId, MatchSummaryDto summary, int inningsNum) {
        List<BattingScorecard> batsmen = battingScorecardRepository.findByInningsIdOrderByBattingPositionAsc(inningsId);
        batsmen.stream().max(Comparator.comparingInt(BattingScorecard::getRuns)).ifPresent(b -> {
            if (inningsNum == 1) {
                summary.setTopBatter1(b.getPlayer().getName());
                summary.setTopBatter1Stats(b.getRuns() + " (" + b.getBallsFaced() + "b, " + b.getFours() + "x4, " + b.getSixes() + "x6)");
            } else {
                summary.setTopBatter2(b.getPlayer().getName());
                summary.setTopBatter2Stats(b.getRuns() + " (" + b.getBallsFaced() + "b, " + b.getFours() + "x4, " + b.getSixes() + "x6)");
            }
        });

        List<BowlingScorecard> bowlers = bowlingScorecardRepository.findByInningsId(inningsId);
        bowlers.stream().max(Comparator.comparingInt(BowlingScorecard::getWicketsTaken)
                .thenComparing(Comparator.comparingInt(BowlingScorecard::getRunsConceded).reversed())).ifPresent(bw -> {
            if (inningsNum == 1) {
                summary.setTopBowler1(bw.getPlayer().getName());
                summary.setTopBowler1Stats(bw.getWicketsTaken() + "/" + bw.getRunsConceded() + " (" + bw.getOversFormatted() + " ov)");
            } else {
                summary.setTopBowler2(bw.getPlayer().getName());
                summary.setTopBowler2Stats(bw.getWicketsTaken() + "/" + bw.getRunsConceded() + " (" + bw.getOversFormatted() + " ov)");
            }
        });
    }

    private List<MatchSummaryDto.OverRunProgression> calculateOverProgression(Long inningsId) {
        List<BallEvent> events = ballEventRepository.findByInningsIdOrderByTotalLegalBallsAtPointAscIdAsc(inningsId);
        Map<Integer, MatchSummaryDto.OverRunProgression> progressionMap = new LinkedHashMap<>();

        for (BallEvent be : events) {
            int over = be.getOverNumber() + 1;
            progressionMap.put(over, new MatchSummaryDto.OverRunProgression(over, be.getScoreAfterBall(), be.getWicketsAfterBall()));
        }
        return new ArrayList<>(progressionMap.values());
    }

    public List<BallEvent> getBallCommentary(Long matchId, int page, int size) {
        Innings currentInnings = inningsRepository
                .findByMatchIdAndInningsNumber(matchId, 2)
                .orElseGet(() -> inningsRepository.findByMatchIdAndInningsNumber(matchId, 1).orElse(null));

        if (currentInnings == null) {
            return Collections.emptyList();
        }
        return ballEventRepository.findByInningsIdOrderByIdDesc(currentInnings.getId(), PageRequest.of(page, size));
    }

    @Transactional
    public Match createMatch(CreateMatchRequest request) {
        Team team1 = teamRepository.findById(request.getTeam1Id())
                .orElseThrow(() -> new IllegalArgumentException("Team 1 not found: " + request.getTeam1Id()));
        Team team2 = teamRepository.findById(request.getTeam2Id())
                .orElseThrow(() -> new IllegalArgumentException("Team 2 not found: " + request.getTeam2Id()));

        Team tossWinner = (request.getTossWinnerId() != null)
                ? teamRepository.findById(request.getTossWinnerId()).orElse(team1)
                : team1;

        TossDecision tossDecision = (request.getTossDecision() != null)
                ? request.getTossDecision()
                : TossDecision.BAT;

        Match match = new Match();
        match.setTitle(request.getTitle() != null ? request.getTitle() : team1.getName() + " vs " + team2.getName());
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setTossWinner(tossWinner);
        match.setTossDecision(tossDecision);
        match.setMatchType(request.getMatchType());
        match.setTotalOversLimit(request.getTotalOversLimit() > 0 ? request.getTotalOversLimit() : 20);
        match.setStatus(MatchStatus.LIVE);
        match.setVenue(request.getVenue() != null ? request.getVenue() : "International Cricket Stadium");
        match.setCity(request.getCity() != null ? request.getCity() : "Melbourne");
        match.setSimulationSpeedMs(2500);
        match = matchRepository.save(match);

        // Determine batting team for innings 1
        Team battingTeamInnings1;
        Team bowlingTeamInnings1;
        if (tossWinner.getId().equals(team1.getId())) {
            if (tossDecision == TossDecision.BAT) {
                battingTeamInnings1 = team1;
                bowlingTeamInnings1 = team2;
            } else {
                battingTeamInnings1 = team2;
                bowlingTeamInnings1 = team1;
            }
        } else {
            if (tossDecision == TossDecision.BAT) {
                battingTeamInnings1 = team2;
                bowlingTeamInnings1 = team1;
            } else {
                battingTeamInnings1 = team1;
                bowlingTeamInnings1 = team2;
            }
        }

        Innings inn1 = new Innings(match.getId(), 1, battingTeamInnings1, bowlingTeamInnings1, null);
        inn1 = inningsRepository.save(inn1);

        // Populate batting scorecards for Innings 1
        List<Player> battingPlayers = playerRepository.findByTeamId(battingTeamInnings1.getId());
        for (int i = 0; i < battingPlayers.size(); i++) {
            BattingScorecard bsc = new BattingScorecard(inn1.getId(), battingPlayers.get(i), i + 1);
            if (i == 0) {
                bsc.setStriker(true);
                inn1.setStrikerPlayerId(battingPlayers.get(i).getId());
            } else if (i == 1) {
                bsc.setNonStriker(true);
                inn1.setNonStrikerPlayerId(battingPlayers.get(i).getId());
            }
            battingScorecardRepository.save(bsc);
        }

        // Populate bowling scorecards for Innings 1
        List<Player> bowlingPlayers = playerRepository.findByTeamId(bowlingTeamInnings1.getId());
        Player openingBowler = null;
        for (Player bp : bowlingPlayers) {
            BowlingScorecard bsc = new BowlingScorecard(inn1.getId(), bp);
            if (openingBowler == null && (bp.getRole() == com.cricketscore.model.enums.PlayerRole.BOWLER || bp.getRole() == com.cricketscore.model.enums.PlayerRole.ALL_ROUNDER)) {
                openingBowler = bp;
                bsc.setCurrentBowler(true);
            }
            bowlingScorecardRepository.save(bsc);
        }

        if (openingBowler == null && !bowlingPlayers.isEmpty()) {
            openingBowler = bowlingPlayers.get(bowlingPlayers.size() - 1);
        }
        if (openingBowler != null) {
            inn1.setCurrentBowlerPlayerId(openingBowler.getId());
        }

        inningsRepository.save(inn1);
        return match;
    }

    @Transactional
    public void resetMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        match.setStatus(MatchStatus.LIVE);
        match.setCurrentInningsNumber(1);
        match.setWinner(null);
        match.setWinMargin(null);
        match.setManOfTheMatch(null);
        match.setSimulating(false);
        matchRepository.save(match);

        List<Innings> inningsList = inningsRepository.findByMatchIdOrderByInningsNumberAsc(matchId);
        for (Innings inn : inningsList) {
            ballEventRepository.deleteAll(ballEventRepository.findByInningsIdOrderByTotalLegalBallsAtPointAscIdAsc(inn.getId()));
            battingScorecardRepository.deleteAll(battingScorecardRepository.findByInningsIdOrderByBattingPositionAsc(inn.getId()));
            bowlingScorecardRepository.deleteAll(bowlingScorecardRepository.findByInningsId(inn.getId()));
        }
        inningsRepository.deleteAll(inningsList);

        // Re-initialize innings 1
        Innings inn1 = new Innings(match.getId(), 1, match.getTeam1(), match.getTeam2(), null);
        inn1 = inningsRepository.save(inn1);

        List<Player> batters = playerRepository.findByTeamId(match.getTeam1().getId());
        for (int i = 0; i < batters.size(); i++) {
            BattingScorecard bsc = new BattingScorecard(inn1.getId(), batters.get(i), i + 1);
            if (i == 0) {
                bsc.setStriker(true);
                inn1.setStrikerPlayerId(batters.get(i).getId());
            } else if (i == 1) {
                bsc.setNonStriker(true);
                inn1.setNonStrikerPlayerId(batters.get(i).getId());
            }
            battingScorecardRepository.save(bsc);
        }

        List<Player> bowlers = playerRepository.findByTeamId(match.getTeam2().getId());
        Player openingBowler = bowlers.stream()
                .filter(p -> p.getRole() == com.cricketscore.model.enums.PlayerRole.BOWLER)
                .findFirst().orElse(bowlers.isEmpty() ? null : bowlers.get(0));

        for (Player bp : bowlers) {
            BowlingScorecard bsc = new BowlingScorecard(inn1.getId(), bp);
            if (openingBowler != null && bp.getId().equals(openingBowler.getId())) {
                bsc.setCurrentBowler(true);
            }
            bowlingScorecardRepository.save(bsc);
        }

        if (openingBowler != null) {
            inn1.setCurrentBowlerPlayerId(openingBowler.getId());
        }
        inningsRepository.save(inn1);
    }
}
