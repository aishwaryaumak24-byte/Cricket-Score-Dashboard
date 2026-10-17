package com.cricketscore.service;

import com.cricketscore.dto.LiveMatchUpdateDto;
import com.cricketscore.dto.ScoreBallRequest;
import com.cricketscore.model.*;
import com.cricketscore.model.enums.ExtraType;
import com.cricketscore.model.enums.MatchStatus;
import com.cricketscore.model.enums.WicketType;
import com.cricketscore.repository.*;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;

@Service
public class MatchSimulationService {

    private static final Logger log = LoggerFactory.getLogger(MatchSimulationService.class);

    private final MatchRepository matchRepository;
    private final InningsRepository inningsRepository;
    private final BattingScorecardRepository battingScorecardRepository;
    private final BowlingScorecardRepository bowlingScorecardRepository;
    private final BallEventRepository ballEventRepository;
    private final PlayerRepository playerRepository;
    private final LiveScorePublisher liveScorePublisher;
    private final MatchService matchService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final Map<Long, ScheduledFuture<?>> activeSimulations = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public MatchSimulationService(MatchRepository matchRepository,
                                  InningsRepository inningsRepository,
                                  BattingScorecardRepository battingScorecardRepository,
                                  BowlingScorecardRepository bowlingScorecardRepository,
                                  BallEventRepository ballEventRepository,
                                  PlayerRepository playerRepository,
                                  LiveScorePublisher liveScorePublisher,
                                  @Lazy MatchService matchService) {
        this.matchRepository = matchRepository;
        this.inningsRepository = inningsRepository;
        this.battingScorecardRepository = battingScorecardRepository;
        this.bowlingScorecardRepository = bowlingScorecardRepository;
        this.ballEventRepository = ballEventRepository;
        this.playerRepository = playerRepository;
        this.liveScorePublisher = liveScorePublisher;
        this.matchService = matchService;
    }

    @Transactional
    public synchronized LiveMatchUpdateDto scoreBall(Long matchId, ScoreBallRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        if (match.getStatus() == MatchStatus.COMPLETED) {
            log.info("Match {} is already completed", matchId);
            return matchService.getLiveUpdate(matchId);
        }

        Innings innings = inningsRepository
                .findByMatchIdAndInningsNumber(matchId, match.getCurrentInningsNumber())
                .orElseThrow(() -> new IllegalStateException("Active innings not found for match: " + matchId));

        if (innings.isCompleted()) {
            return matchService.getLiveUpdate(matchId);
        }

        // Fetch active Striker, Non-Striker, Bowler
        BattingScorecard strikerScorecard = battingScorecardRepository
                .findByInningsIdAndPlayerId(innings.getId(), innings.getStrikerPlayerId())
                .orElse(null);

        BattingScorecard nonStrikerScorecard = battingScorecardRepository
                .findByInningsIdAndPlayerId(innings.getId(), innings.getNonStrikerPlayerId())
                .orElse(null);

        BowlingScorecard bowlerScorecard = bowlingScorecardRepository
                .findByInningsIdAndPlayerId(innings.getId(), innings.getCurrentBowlerPlayerId())
                .orElse(null);

        if (strikerScorecard == null || bowlerScorecard == null) {
            throw new IllegalStateException("Striker or Bowler scorecard missing for match " + matchId);
        }

        Player striker = strikerScorecard.getPlayer();
        Player bowler = bowlerScorecard.getPlayer();
        Player nonStriker = nonStrikerScorecard != null ? nonStrikerScorecard.getPlayer() : null;

        boolean isLegalBall = (request.getExtraType() != ExtraType.WIDE && request.getExtraType() != ExtraType.NO_BALL);
        int runsScored = request.getRunsScored();
        int extraRuns = request.getExtraRuns();
        int totalRunsThisBall = runsScored;

        // Process Extras
        if (request.getExtraType() == ExtraType.WIDE) {
            extraRuns = Math.max(1, extraRuns);
            innings.setWides(innings.getWides() + extraRuns);
            innings.setExtrasTotal(innings.getExtrasTotal() + extraRuns);
            bowlerScorecard.setWides(bowlerScorecard.getWides() + 1);
            bowlerScorecard.setRunsConceded(bowlerScorecard.getRunsConceded() + extraRuns);
            totalRunsThisBall = extraRuns;
        } else if (request.getExtraType() == ExtraType.NO_BALL) {
            extraRuns = Math.max(1, extraRuns);
            innings.setNoBalls(innings.getNoBalls() + 1);
            innings.setExtrasTotal(innings.getExtrasTotal() + 1);
            bowlerScorecard.setNoBalls(bowlerScorecard.getNoBalls() + 1);
            bowlerScorecard.setRunsConceded(bowlerScorecard.getRunsConceded() + 1 + runsScored);
            strikerScorecard.setBallsFaced(strikerScorecard.getBallsFaced() + 1);
            strikerScorecard.setRuns(strikerScorecard.getRuns() + runsScored);
            if (runsScored == 4) strikerScorecard.setFours(strikerScorecard.getFours() + 1);
            if (runsScored == 6) strikerScorecard.setSixes(strikerScorecard.getSixes() + 1);
            totalRunsThisBall = 1 + runsScored;
        } else if (request.getExtraType() == ExtraType.BYE) {
            innings.setByes(innings.getByes() + extraRuns);
            innings.setExtrasTotal(innings.getExtrasTotal() + extraRuns);
            strikerScorecard.setBallsFaced(strikerScorecard.getBallsFaced() + 1);
            totalRunsThisBall = extraRuns;
        } else if (request.getExtraType() == ExtraType.LEG_BYE) {
            innings.setLegByes(innings.getLegByes() + extraRuns);
            innings.setExtrasTotal(innings.getExtrasTotal() + extraRuns);
            strikerScorecard.setBallsFaced(strikerScorecard.getBallsFaced() + 1);
            totalRunsThisBall = extraRuns;
        } else {
            // Normal Ball off bat
            strikerScorecard.setBallsFaced(strikerScorecard.getBallsFaced() + 1);
            strikerScorecard.setRuns(strikerScorecard.getRuns() + runsScored);
            if (runsScored == 4) strikerScorecard.setFours(strikerScorecard.getFours() + 1);
            if (runsScored == 6) strikerScorecard.setSixes(strikerScorecard.getSixes() + 1);
            bowlerScorecard.setRunsConceded(bowlerScorecard.getRunsConceded() + runsScored);
            if (runsScored == 0 && !request.isWicket()) {
                bowlerScorecard.setDotBalls(bowlerScorecard.getDotBalls() + 1);
            }
        }

        // Apply total runs to innings
        innings.setTotalRuns(innings.getTotalRuns() + totalRunsThisBall);

        // Update legal balls if valid
        if (isLegalBall) {
            innings.setLegalBalls(innings.getLegalBalls() + 1);
            bowlerScorecard.setLegalBalls(bowlerScorecard.getLegalBalls() + 1);
        }

        // Process Wicket
        boolean wicketFell = request.isWicket();
        if (wicketFell) {
            innings.setTotalWickets(innings.getTotalWickets() + 1);
            strikerScorecard.setOut(true);
            strikerScorecard.setStriker(false);
            strikerScorecard.setDismissalType(request.getWicketType());
            strikerScorecard.setBowlerName(bowler.getName());
            strikerScorecard.setDismissalInfo(generateDismissalText(request.getWicketType(), bowler.getName()));

            if (request.getWicketType() != WicketType.RUN_OUT) {
                bowlerScorecard.setWicketsTaken(bowlerScorecard.getWicketsTaken() + 1);
            }

            // Bring in new batsman if wickets < 10
            if (innings.getTotalWickets() < 10) {
                List<BattingScorecard> remainingBatsmen = battingScorecardRepository
                        .findByInningsIdOrderByBattingPositionAsc(innings.getId());

                BattingScorecard nextBatter = remainingBatsmen.stream()
                        .filter(b -> !b.isOut() && !b.isStriker() && !b.isNonStriker() && b.getBallsFaced() == 0)
                        .findFirst()
                        .orElse(null);

                if (nextBatter != null) {
                    nextBatter.setStriker(true);
                    innings.setStrikerPlayerId(nextBatter.getPlayer().getId());
                    battingScorecardRepository.save(nextBatter);
                }
            }
        }

        // Strike rotation for odd runs (1, 3)
        if (runsScored % 2 != 0 || (request.getExtraType() != ExtraType.NONE && totalRunsThisBall % 2 != 0)) {
            swapStrike(innings, strikerScorecard, nonStrikerScorecard);
        }

        // Calculate over and ball numbers
        int currentOver = (innings.getLegalBalls() - (isLegalBall ? 1 : 0)) / 6;
        int ballInOver = (innings.getLegalBalls() - (isLegalBall ? 1 : 0)) % 6 + (isLegalBall ? 1 : 0);
        if (ballInOver == 0 && isLegalBall) ballInOver = 6;

        // Generate Ball Display
        String ballDisplay = generateBallDisplay(request, runsScored, totalRunsThisBall);

        // Generate commentary text
        String commentary = (request.getCustomCommentary() != null && !request.getCustomCommentary().isBlank())
                ? request.getCustomCommentary()
                : generateCommentary(striker.getName(), bowler.getName(), runsScored, request, innings.getOversFormatted());

        // Create and persist BallEvent
        BallEvent ballEvent = new BallEvent();
        ballEvent.setMatchId(matchId);
        ballEvent.setInningsId(innings.getId());
        ballEvent.setOverNumber(currentOver);
        ballEvent.setBallNumberInOver(ballInOver);
        ballEvent.setTotalLegalBallsAtPoint(innings.getLegalBalls());
        ballEvent.setRunsScored(runsScored);
        ballEvent.setExtraType(request.getExtraType());
        ballEvent.setExtraRuns(extraRuns);
        ballEvent.setWicket(wicketFell);
        ballEvent.setWicketType(request.getWicketType());
        ballEvent.setBatsman(striker);
        ballEvent.setBowler(bowler);
        ballEvent.setNonStriker(nonStriker);
        ballEvent.setBallDisplay(ballDisplay);
        ballEvent.setCommentaryText(commentary);
        ballEvent.setScoreAfterBall(innings.getTotalRuns());
        ballEvent.setWicketsAfterBall(innings.getTotalWickets());
        ballEventRepository.save(ballEvent);

        // Check Over completion
        if (isLegalBall && innings.getLegalBalls() % 6 == 0) {
            // Swap strike at end of over
            swapStrike(innings, strikerScorecard, nonStrikerScorecard);

            // Rotate Bowler
            rotateBowler(innings, bowlerScorecard);
        }

        // Check Innings 1 / Innings 2 completion condition
        checkMatchProgress(match, innings);

        // Save states
        battingScorecardRepository.save(strikerScorecard);
        if (nonStrikerScorecard != null) {
            battingScorecardRepository.save(nonStrikerScorecard);
        }
        bowlingScorecardRepository.save(bowlerScorecard);
        inningsRepository.save(innings);
        matchRepository.save(match);

        // Broadcast to WebSocket clients
        LiveMatchUpdateDto updateDto = matchService.getLiveUpdate(matchId);
        liveScorePublisher.publishMatchUpdate(updateDto);

        return updateDto;
    }

    private void swapStrike(Innings innings, BattingScorecard striker, BattingScorecard nonStriker) {
        if (striker == null || nonStriker == null || striker.isOut() || nonStriker.isOut()) return;
        Long tempId = innings.getStrikerPlayerId();
        innings.setStrikerPlayerId(innings.getNonStrikerPlayerId());
        innings.setNonStrikerPlayerId(tempId);

        striker.setStriker(false);
        striker.setNonStriker(true);
        nonStriker.setStriker(true);
        nonStriker.setNonStriker(false);
    }

    private void rotateBowler(Innings innings, BowlingScorecard currentBowler) {
        currentBowler.setCurrentBowler(false);

        List<BowlingScorecard> bowlers = bowlingScorecardRepository.findByInningsId(innings.getId());
        List<BowlingScorecard> eligibleBowlers = bowlers.stream()
                .filter(b -> !b.getPlayer().getId().equals(currentBowler.getPlayer().getId()))
                .filter(b -> b.getPlayer().getRole() == com.cricketscore.model.enums.PlayerRole.BOWLER ||
                        b.getPlayer().getRole() == com.cricketscore.model.enums.PlayerRole.ALL_ROUNDER)
                .toList();

        if (eligibleBowlers.isEmpty()) {
            eligibleBowlers = bowlers.stream()
                    .filter(b -> !b.getPlayer().getId().equals(currentBowler.getPlayer().getId()))
                    .toList();
        }

        if (!eligibleBowlers.isEmpty()) {
            // Pick next bowler in rotation
            BowlingScorecard nextBowler = eligibleBowlers.get(random.nextInt(eligibleBowlers.size()));
            nextBowler.setCurrentBowler(true);
            innings.setCurrentBowlerPlayerId(nextBowler.getPlayer().getId());
            bowlingScorecardRepository.save(nextBowler);
        }
    }

    private void checkMatchProgress(Match match, Innings innings) {
        int maxLegalBalls = match.getTotalOversLimit() * 6;
        boolean allOut = innings.getTotalWickets() >= 10;
        boolean oversExhausted = innings.getLegalBalls() >= maxLegalBalls;

        if (match.getCurrentInningsNumber() == 1) {
            if (allOut || oversExhausted) {
                innings.setCompleted(true);
                int target = innings.getTotalRuns() + 1;
                match.setTarget(target);
                match.setCurrentInningsNumber(2);

                // Start Innings 2
                startSecondInnings(match, target);
                log.info("Innings 1 finished with {}/{}. Target for Innings 2: {}",
                        innings.getTotalRuns(), innings.getTotalWickets(), target);
            }
        } else if (match.getCurrentInningsNumber() == 2) {
            Integer target = match.getTarget();
            if (target != null && innings.getTotalRuns() >= target) {
                // Batting team wins by wickets
                innings.setCompleted(true);
                match.setStatus(MatchStatus.COMPLETED);
                match.setWinner(innings.getBattingTeam());
                int wicketsLeft = 10 - innings.getTotalWickets();
                match.setWinMargin(innings.getBattingTeam().getName() + " won by " + wicketsLeft + " wicket" + (wicketsLeft > 1 ? "s" : ""));
                concludeMatch(match);
            } else if (allOut || oversExhausted) {
                innings.setCompleted(true);
                match.setStatus(MatchStatus.COMPLETED);
                if (target != null && innings.getTotalRuns() == target - 1) {
                    match.setWinMargin("Match Tied!");
                } else {
                    int runDeficit = (target != null ? target - 1 : 0) - innings.getTotalRuns();
                    match.setWinner(innings.getBowlingTeam());
                    match.setWinMargin(innings.getBowlingTeam().getName() + " won by " + runDeficit + " run" + (runDeficit > 1 ? "s" : ""));
                }
                concludeMatch(match);
            }
        }
    }

    private void startSecondInnings(Match match, int target) {
        Team battingTeamInnings2 = (match.getTeam1().getId().equals(match.getTossWinner().getId()))
                ? (match.getTossDecision() == com.cricketscore.model.enums.TossDecision.BAT ? match.getTeam2() : match.getTeam1())
                : (match.getTossDecision() == com.cricketscore.model.enums.TossDecision.BAT ? match.getTeam1() : match.getTeam2());

        Team bowlingTeamInnings2 = battingTeamInnings2.getId().equals(match.getTeam1().getId()) ? match.getTeam2() : match.getTeam1();

        Innings inn2 = new Innings(match.getId(), 2, battingTeamInnings2, bowlingTeamInnings2, target);
        inn2 = inningsRepository.save(inn2);

        // Populate batting scorecards for Innings 2
        List<Player> batters = playerRepository.findByTeamId(battingTeamInnings2.getId());
        for (int i = 0; i < batters.size(); i++) {
            BattingScorecard bsc = new BattingScorecard(inn2.getId(), batters.get(i), i + 1);
            if (i == 0) {
                bsc.setStriker(true);
                inn2.setStrikerPlayerId(batters.get(i).getId());
            } else if (i == 1) {
                bsc.setNonStriker(true);
                inn2.setNonStrikerPlayerId(batters.get(i).getId());
            }
            battingScorecardRepository.save(bsc);
        }

        // Populate bowling scorecards for Innings 2
        List<Player> bowlers = playerRepository.findByTeamId(bowlingTeamInnings2.getId());
        Player openingBowler = bowlers.stream()
                .filter(p -> p.getRole() == com.cricketscore.model.enums.PlayerRole.BOWLER)
                .findFirst().orElse(!bowlers.isEmpty() ? bowlers.get(0) : null);

        for (Player bp : bowlers) {
            BowlingScorecard bsc = new BowlingScorecard(inn2.getId(), bp);
            if (openingBowler != null && bp.getId().equals(openingBowler.getId())) {
                bsc.setCurrentBowler(true);
            }
            bowlingScorecardRepository.save(bsc);
        }

        if (openingBowler != null) {
            inn2.setCurrentBowlerPlayerId(openingBowler.getId());
        }
        inningsRepository.save(inn2);
    }

    private void concludeMatch(Match match) {
        pauseSimulation(match.getId());
        match.setSimulating(false);

        // Determine Player of the Match
        List<Innings> inningsList = inningsRepository.findByMatchIdOrderByInningsNumberAsc(match.getId());
        Player topPlayer = null;
        int maxScore = -1;

        for (Innings inn : inningsList) {
            List<BattingScorecard> batList = battingScorecardRepository.findByInningsIdOrderByBattingPositionAsc(inn.getId());
            for (BattingScorecard b : batList) {
                int impact = b.getRuns() + (b.getSixes() * 3);
                if (impact > maxScore) {
                    maxScore = impact;
                    topPlayer = b.getPlayer();
                }
            }
            List<BowlingScorecard> bowlList = bowlingScorecardRepository.findByInningsId(inn.getId());
            for (BowlingScorecard bw : bowlList) {
                int impact = (bw.getWicketsTaken() * 25) + (bw.getDotBalls() * 2);
                if (impact > maxScore) {
                    maxScore = impact;
                    topPlayer = bw.getPlayer();
                }
            }
        }

        if (topPlayer != null) {
            match.setManOfTheMatch(topPlayer.getName());
        }
        log.info("Match {} completed! Result: {}. Player of the Match: {}",
                match.getId(), match.getWinMargin(), match.getManOfTheMatch());
    }

    public synchronized LiveMatchUpdateDto simulateNextBall(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        if (match.getStatus() == MatchStatus.COMPLETED) {
            pauseSimulation(matchId);
            return matchService.getLiveUpdate(matchId);
        }

        ScoreBallRequest request = generateProbabilisticBall();
        return scoreBall(matchId, request);
    }

    private ScoreBallRequest generateProbabilisticBall() {
        ScoreBallRequest request = new ScoreBallRequest();
        int roll = random.nextInt(100);

        if (roll < 36) {
            // Dot ball
            request.setRunsScored(0);
        } else if (roll < 68) {
            // 1 run
            request.setRunsScored(1);
        } else if (roll < 77) {
            // 2 runs
            request.setRunsScored(2);
        } else if (roll < 79) {
            // 3 runs
            request.setRunsScored(3);
        } else if (roll < 90) {
            // Boundary: 4 runs!
            request.setRunsScored(4);
        } else if (roll < 95) {
            // Six: 6 runs!
            request.setRunsScored(6);
        } else if (roll < 98) {
            // Wicket!
            request.setWicket(true);
            request.setRunsScored(0);
            int wRoll = random.nextInt(5);
            if (wRoll == 0) request.setWicketType(WicketType.BOWLED);
            else if (wRoll == 1) request.setWicketType(WicketType.LBW);
            else if (wRoll == 2) request.setWicketType(WicketType.CAUGHT);
            else if (wRoll == 3) request.setWicketType(WicketType.CAUGHT);
            else request.setWicketType(WicketType.RUN_OUT);
        } else if (roll < 99) {
            // Wide
            request.setExtraType(ExtraType.WIDE);
            request.setExtraRuns(1);
            request.setRunsScored(0);
        } else {
            // No ball
            request.setExtraType(ExtraType.NO_BALL);
            request.setRunsScored(random.nextBoolean() ? 4 : 1);
        }

        return request;
    }

    public synchronized void startSimulation(Long matchId, Long speedMs) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        if (match.getStatus() == MatchStatus.COMPLETED) {
            log.info("Cannot simulate completed match {}", matchId);
            return;
        }

        pauseSimulation(matchId); // Cancel any previous timer

        long delay = (speedMs != null && speedMs >= 500) ? speedMs : 2500;
        match.setSimulating(true);
        match.setSimulationSpeedMs(delay);
        matchRepository.save(match);

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                simulateNextBall(matchId);
            } catch (Exception e) {
                log.error("Simulation error for match {}: {}", matchId, e.getMessage());
            }
        }, 500, delay, TimeUnit.MILLISECONDS);

        activeSimulations.put(matchId, future);
        log.info("Started real-time match simulation for match ID {} with speed {}ms", matchId, delay);
    }

    public synchronized void pauseSimulation(Long matchId) {
        ScheduledFuture<?> future = activeSimulations.remove(matchId);
        if (future != null) {
            future.cancel(false);
            log.info("Paused simulation for match ID {}", matchId);
        }

        matchRepository.findById(matchId).ifPresent(m -> {
            m.setSimulating(false);
            matchRepository.save(m);
        });
    }

    private String generateBallDisplay(ScoreBallRequest request, int runsScored, int totalRunsThisBall) {
        if (request.isWicket()) {
            return "W";
        }
        if (request.getExtraType() == ExtraType.WIDE) {
            return (totalRunsThisBall > 1 ? totalRunsThisBall : "") + "Wd";
        }
        if (request.getExtraType() == ExtraType.NO_BALL) {
            return "Nb" + (runsScored > 0 ? "+" + runsScored : "");
        }
        if (request.getExtraType() == ExtraType.BYE) {
            return totalRunsThisBall + "B";
        }
        if (request.getExtraType() == ExtraType.LEG_BYE) {
            return totalRunsThisBall + "Lb";
        }
        return String.valueOf(runsScored);
    }

    private String generateDismissalText(WicketType type, String bowlerName) {
        return switch (type) {
            case BOWLED -> "b " + bowlerName;
            case LBW -> "lbw b " + bowlerName;
            case CAUGHT -> "c sub b " + bowlerName;
            case STUMPED -> "st †keeper b " + bowlerName;
            case RUN_OUT -> "run out (direct hit)";
            case HIT_WICKET -> "hit wicket b " + bowlerName;
            default -> "out b " + bowlerName;
        };
    }

    private String generateCommentary(String batter, String bowler, int runs, ScoreBallRequest req, String over) {
        if (req.isWicket()) {
            return switch (req.getWicketType()) {
                case BOWLED -> bowler + " to " + batter + ", OUT! CLEAN BOWLED! A searing yorker smashes straight into the middle stump! Electric atmosphere!";
                case LBW -> bowler + " to " + batter + ", OUT! LBW! Trapped plumb in front! Umpire raises the finger without hesitation!";
                case CAUGHT -> bowler + " to " + batter + ", OUT! CAUGHT! Sliced high in the air and safely pocketed! Huge breakthrough!";
                case RUN_OUT -> bowler + " to " + batter + ", OUT! RUN OUT! Confusion in the middle and a laser direct hit from the deep!";
                default -> bowler + " to " + batter + ", OUT! The dismissal sends the fielding side into joyous celebrations!";
            };
        }
        if (req.getExtraType() == ExtraType.WIDE) {
            return bowler + " to " + batter + ", WIDE! Slipped way outside off-stump, umpire stretches both arms.";
        }
        if (req.getExtraType() == ExtraType.NO_BALL) {
            return bowler + " to " + batter + ", NO BALL! Overstepping the crease! Free hit coming up next!";
        }
        return switch (runs) {
            case 0 -> bowler + " to " + batter + ", no run. Good length on off stump, solidly defended towards cover.";
            case 1 -> bowler + " to " + batter + ", 1 run. Tucked gently through midwicket for a sharp single.";
            case 2 -> bowler + " to " + batter + ", 2 runs. Pushed into the gap at deep cover, excellent running between the wickets.";
            case 3 -> bowler + " to " + batter + ", 3 runs. Superb timing through backward point, saved right on the boundary rope!";
            case 4 -> bowler + " to " + batter + ", FOUR! CRACKING SHOT! Leans into the drive and strokes it gloriously through the covers!";
            case 6 -> bowler + " to " + batter + ", SIX! HIGH AND HANDSOME! Dances down the pitch and launches it into the second tier!";
            default -> bowler + " to " + batter + ", " + runs + " runs scored.";
        };
    }

    @PreDestroy
    public void cleanup() {
        scheduler.shutdown();
    }
}
