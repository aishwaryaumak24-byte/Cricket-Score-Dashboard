package com.cricketscore.config;

import com.cricketscore.dto.ScoreBallRequest;
import com.cricketscore.model.*;
import com.cricketscore.model.enums.*;
import com.cricketscore.repository.*;
import com.cricketscore.service.MatchSimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final InningsRepository inningsRepository;
    private final BattingScorecardRepository battingScorecardRepository;
    private final BowlingScorecardRepository bowlingScorecardRepository;
    private final BallEventRepository ballEventRepository;
    private final MatchSimulationService simulationService;

    public DataInitializer(TeamRepository teamRepository,
                           PlayerRepository playerRepository,
                           MatchRepository matchRepository,
                           InningsRepository inningsRepository,
                           BattingScorecardRepository battingScorecardRepository,
                           BowlingScorecardRepository bowlingScorecardRepository,
                           BallEventRepository ballEventRepository,
                           MatchSimulationService simulationService) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.inningsRepository = inningsRepository;
        this.battingScorecardRepository = battingScorecardRepository;
        this.bowlingScorecardRepository = bowlingScorecardRepository;
        this.ballEventRepository = ballEventRepository;
        this.simulationService = simulationService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing cricket database with realistic teams, players, and blockbuster matches...");

        // 1. Teams
        Team india = teamRepository.save(new Team("India", "IND", "🇮🇳", "#0055A5", "#FF9933", "India"));
        Team australia = teamRepository.save(new Team("Australia", "AUS", "🇦🇺", "#FFCC00", "#004D25", "Australia"));
        Team england = teamRepository.save(new Team("England", "ENG", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", "#002B49", "#CF142B", "England"));
        Team southAfrica = teamRepository.save(new Team("South Africa", "SA", "🇿🇦", "#007A3D", "#FFB81C", "South Africa"));
        Team pakistan = teamRepository.save(new Team("Pakistan", "PAK", "🇵🇰", "#01411C", "#98CE00", "Pakistan"));
        Team newZealand = teamRepository.save(new Team("New Zealand", "NZ", "🇳🇿", "#1E1E1E", "#4A90E2", "New Zealand"));
        Team csk = teamRepository.save(new Team("Chennai Super Kings", "CSK", "🦁", "#FDB913", "#004B8D", "India"));
        Team mi = teamRepository.save(new Team("Mumbai Indians", "MI", "🌀", "#004BA0", "#D1AB3E", "India"));

        // 2. Players - India
        List<Player> indSquad = createIndianSquad(india);
        // Australia
        List<Player> ausSquad = createAussieSquad(australia);
        // England
        List<Player> engSquad = createEngSquad(england);
        // South Africa
        List<Player> saSquad = createSASquad(southAfrica);
        // Pakistan
        createPakSquad(pakistan);
        // New Zealand
        createNZSquad(newZealand);

        // 3. Match 1: India vs Australia (Live T20 World Cup Thriller)
        Match match1 = createIndVsAusMatch(india, australia, indSquad, ausSquad);

        // 4. Match 2: England vs South Africa (Live match)
        createEngVsSAMatch(england, southAfrica, engSquad, saSquad);

        // 5. Match 3: Pakistan vs New Zealand (Completed match)
        createPakVsNZMatch(pakistan, newZealand);

        // 6. Match 4: CSK vs MI (Upcoming Scheduled)
        createCskVsMiMatch(csk, mi);

        log.info("Initialization complete! Demo matches seeded successfully.");
    }

    private List<Player> createIndianSquad(Team team) {
        List<Player> players = new ArrayList<>();
        players.add(savePlayer("Rohit Sharma", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm offbreak", 45, 151, 4039, 121, 5, 31, 1, "1/22", 7.8, 140.9));
        players.add(savePlayer("Virat Kohli", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm medium", 18, 125, 4188, 122, 1, 38, 4, "1/13", 8.1, 137.0));
        players.add(savePlayer("Suryakumar Yadav", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm offbreak", 63, 68, 2340, 117, 4, 19, 0, "-", 0, 167.7));
        players.add(savePlayer("Rishabh Pant", team, PlayerRole.WICKET_KEEPER, "Left-hand bat", "None", 17, 74, 1158, 65, 0, 3, 0, "-", 0, 126.3));
        players.add(savePlayer("Hardik Pandya", team, PlayerRole.ALL_ROUNDER, "Right-hand bat", "Right-arm fast-medium", 33, 100, 1492, 71, 0, 4, 84, "4/16", 8.1, 139.8));
        players.add(savePlayer("Ravindra Jadeja", team, PlayerRole.ALL_ROUNDER, "Left-hand bat", "Slow left-arm orthodox", 8, 74, 515, 46, 0, 0, 54, "3/15", 7.1, 127.2));
        players.add(savePlayer("Axar Patel", team, PlayerRole.ALL_ROUNDER, "Left-hand bat", "Slow left-arm orthodox", 20, 60, 450, 65, 0, 1, 58, "3/9", 7.3, 144.5));
        players.add(savePlayer("Kuldeep Yadav", team, PlayerRole.BOWLER, "Left-hand bat", "Left-arm wrist spin", 23, 35, 46, 23, 0, 0, 69, "5/17", 6.7, 95.0));
        players.add(savePlayer("Jasprit Bumrah", team, PlayerRole.BOWLER, "Right-hand bat", "Right-arm fast", 93, 70, 8, 7, 0, 0, 89, "3/7", 6.27, 65.0));
        players.add(savePlayer("Arshdeep Singh", team, PlayerRole.BOWLER, "Left-hand bat", "Left-arm medium-fast", 2, 52, 35, 12, 0, 0, 79, "4/9", 8.3, 80.0));
        players.add(savePlayer("Mohammed Siraj", team, PlayerRole.BOWLER, "Right-hand bat", "Right-arm fast", 13, 16, 12, 7, 0, 0, 14, "4/17", 7.8, 60.0));
        return players;
    }

    private List<Player> createAussieSquad(Team team) {
        List<Player> players = new ArrayList<>();
        players.add(savePlayer("Travis Head", team, PlayerRole.BATSMAN, "Left-hand bat", "Right-arm offbreak", 62, 38, 1072, 91, 0, 5, 1, "1/10", 8.5, 158.4));
        players.add(savePlayer("David Warner", team, PlayerRole.BATSMAN, "Left-hand bat", "Right-arm legbreak", 31, 110, 3277, 100, 1, 28, 0, "-", 0, 142.5));
        players.add(savePlayer("Mitchell Marsh", team, PlayerRole.ALL_ROUNDER, "Right-hand bat", "Right-arm medium", 8, 54, 1432, 92, 0, 9, 17, "3/24", 8.2, 135.2));
        players.add(savePlayer("Glenn Maxwell", team, PlayerRole.ALL_ROUNDER, "Right-hand bat", "Right-arm offbreak", 32, 106, 2600, 145, 5, 11, 43, "3/10", 7.5, 155.5));
        players.add(savePlayer("Marcus Stoinis", team, PlayerRole.ALL_ROUNDER, "Right-hand bat", "Right-arm medium-fast", 17, 68, 1124, 78, 0, 5, 45, "3/18", 8.4, 146.2));
        players.add(savePlayer("Tim David", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm offbreak", 85, 39, 1018, 92, 0, 4, 5, "1/16", 9.2, 163.5));
        players.add(savePlayer("Matthew Wade", team, PlayerRole.WICKET_KEEPER, "Left-hand bat", "Right-arm medium", 13, 92, 1202, 80, 0, 3, 0, "-", 0, 134.1));
        players.add(savePlayer("Pat Cummins", team, PlayerRole.BOWLER, "Right-hand bat", "Right-arm fast", 30, 57, 180, 28, 0, 0, 66, "3/15", 7.4, 120.0));
        players.add(savePlayer("Mitchell Starc", team, PlayerRole.BOWLER, "Left-hand bat", "Left-arm fast", 56, 65, 94, 14, 0, 0, 79, "4/20", 7.7, 85.0));
        players.add(savePlayer("Adam Zampa", team, PlayerRole.BOWLER, "Right-hand bat", "Right-arm legbreak", 88, 87, 48, 13, 0, 0, 105, "5/19", 7.2, 70.0));
        players.add(savePlayer("Josh Hazlewood", team, PlayerRole.BOWLER, "Left-hand bat", "Right-arm fast-medium", 38, 51, 22, 11, 0, 0, 67, "4/12", 7.1, 75.0));
        return players;
    }

    private List<Player> createEngSquad(Team team) {
        List<Player> players = new ArrayList<>();
        players.add(savePlayer("Jos Buttler", team, PlayerRole.WICKET_KEEPER, "Right-hand bat", "None", 63, 124, 3264, 101, 1, 24, 0, "-", 0, 146.3));
        players.add(savePlayer("Phil Salt", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm offbreak", 28, 38, 1090, 119, 3, 4, 0, "-", 0, 165.1));
        players.add(savePlayer("Harry Brook", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm medium", 88, 39, 702, 81, 0, 3, 0, "-", 0, 145.4));
        players.add(savePlayer("Liam Livingstone", team, PlayerRole.ALL_ROUNDER, "Right-hand bat", "Right-arm legbreak", 23, 48, 763, 103, 1, 2, 25, "3/17", 8.3, 150.2));
        players.add(savePlayer("Sam Curran", team, PlayerRole.ALL_ROUNDER, "Left-hand bat", "Left-arm medium-fast", 58, 55, 332, 41, 0, 0, 52, "5/10", 8.2, 125.0));
        players.add(savePlayer("Jofra Archer", team, PlayerRole.BOWLER, "Right-hand bat", "Right-arm fast", 22, 28, 45, 17, 0, 0, 35, "4/33", 7.5, 90.0));
        players.add(savePlayer("Adil Rashid", team, PlayerRole.BOWLER, "Right-hand bat", "Right-arm legbreak", 95, 114, 120, 22, 0, 0, 118, "4/2", 7.3, 80.0));
        return players;
    }

    private List<Player> createSASquad(Team team) {
        List<Player> players = new ArrayList<>();
        players.add(savePlayer("Quinton de Kock", team, PlayerRole.WICKET_KEEPER, "Left-hand bat", "None", 12, 92, 2584, 100, 1, 16, 0, "-", 0, 138.3));
        players.add(savePlayer("Reeza Hendricks", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm offbreak", 17, 69, 1957, 83, 0, 17, 0, "-", 0, 131.0));
        players.add(savePlayer("Aiden Markram", team, PlayerRole.ALL_ROUNDER, "Right-hand bat", "Right-arm offbreak", 4, 52, 1312, 70, 0, 11, 15, "3/21", 7.6, 145.8));
        players.add(savePlayer("Heinrich Klaasen", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm offbreak", 45, 54, 912, 81, 0, 5, 0, "-", 0, 142.5));
        players.add(savePlayer("David Miller", team, PlayerRole.BATSMAN, "Left-hand bat", "Right-arm offbreak", 10, 125, 2437, 106, 2, 7, 0, "-", 0, 140.6));
        players.add(savePlayer("Kagiso Rabada", team, PlayerRole.BOWLER, "Left-hand bat", "Right-arm fast", 25, 65, 140, 22, 0, 0, 71, "3/18", 8.4, 95.0));
        players.add(savePlayer("Anrich Nortje", team, PlayerRole.BOWLER, "Right-hand bat", "Right-arm fast", 20, 41, 19, 7, 0, 0, 53, "4/7", 7.1, 70.0));
        return players;
    }

    private void createPakSquad(Team team) {
        savePlayer("Babar Azam", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm offbreak", 56, 123, 4145, 122, 3, 36, 0, "-", 0, 129.1);
        savePlayer("Mohammad Rizwan", team, PlayerRole.WICKET_KEEPER, "Right-hand bat", "Right-arm medium", 16, 102, 3313, 104, 1, 29, 0, "-", 0, 126.5);
        savePlayer("Shaheen Afridi", team, PlayerRole.BOWLER, "Left-hand bat", "Left-arm fast", 10, 70, 160, 23, 0, 0, 96, "4/22", 7.7, 115.0);
        savePlayer("Haris Rauf", team, PlayerRole.BOWLER, "Right-hand bat", "Right-arm fast", 150, 72, 35, 13, 0, 0, 102, "4/18", 8.2, 75.0);
        savePlayer("Shadab Khan", team, PlayerRole.ALL_ROUNDER, "Right-hand bat", "Right-arm legbreak", 7, 104, 655, 52, 0, 1, 107, "4/8", 7.1, 141.0);
    }

    private void createNZSquad(Team team) {
        savePlayer("Kane Williamson", team, PlayerRole.BATSMAN, "Right-hand bat", "Right-arm offbreak", 22, 93, 2575, 95, 0, 18, 6, "2/16", 7.3, 123.1);
        savePlayer("Devon Conway", team, PlayerRole.WICKET_KEEPER, "Left-hand bat", "Right-arm medium", 88, 50, 1408, 99, 0, 10, 0, "-", 0, 128.5);
        savePlayer("Glenn Phillips", team, PlayerRole.ALL_ROUNDER, "Right-hand bat", "Right-arm offbreak", 23, 78, 1875, 108, 2, 10, 19, "3/11", 7.5, 143.0);
        savePlayer("Trent Boult", team, PlayerRole.BOWLER, "Right-hand bat", "Left-arm fast-medium", 18, 61, 38, 16, 0, 0, 83, "4/13", 7.8, 80.0);
        savePlayer("Mitchell Santner", team, PlayerRole.ALL_ROUNDER, "Left-hand bat", "Slow left-arm orthodox", 74, 104, 674, 77, 0, 1, 115, "4/11", 7.0, 122.0);
    }

    private Player savePlayer(String name, Team team, PlayerRole role, String bat, String bowl, int num,
                              int matches, int runs, int hs, int centuries, int fifties, int wickets,
                              String bbf, double econ, double sr) {
        Player p = new Player(name, team.getId(), team.getName(), role, bat, bowl, num);
        p.setMatchesPlayed(matches);
        p.setTotalRuns(runs);
        p.setHighestScore(hs);
        p.setCenturies(centuries);
        p.setHalfCenturies(fifties);
        p.setTotalWickets(wickets);
        p.setBestBowlingFigures(bbf);
        p.setCareerEconomy(econ);
        p.setCareerStrikeRate(sr);
        return playerRepository.save(p);
    }

    private Match createIndVsAusMatch(Team india, Team australia, List<Player> indSquad, List<Player> ausSquad) {
        Match match = new Match();
        match.setTitle("T20 World Cup Super 8: India vs Australia");
        match.setMatchType(MatchType.T20);
        match.setStatus(MatchStatus.LIVE);
        match.setVenue("Kensington Oval");
        match.setCity("Bridgetown");
        match.setTeam1(india);
        match.setTeam2(australia);
        match.setTossWinner(india);
        match.setTossDecision(TossDecision.BAT);
        match.setTotalOversLimit(20);
        match.setCurrentInningsNumber(2);
        match.setTarget(187); // India scored 186
        match.setSimulating(true); // Auto-live simulation on
        match.setSimulationSpeedMs(2500);
        match = matchRepository.save(match);

        // Innings 1: India (Completed 186/5 in 20.0 overs)
        Innings inn1 = new Innings(match.getId(), 1, india, australia, null);
        inn1.setTotalRuns(186);
        inn1.setTotalWickets(5);
        inn1.setLegalBalls(120);
        inn1.setCompleted(true);
        inn1.setExtrasTotal(11);
        inn1.setWides(7);
        inn1.setNoBalls(2);
        inn1.setByes(1);
        inn1.setLegByes(1);
        inn1 = inningsRepository.save(inn1);

        // Innings 1 Batting Scorecards
        createIndianBattingScorecard(inn1, indSquad);
        // Innings 1 Bowling Scorecards
        createAusBowlingScorecard(inn1, ausSquad);

        // Innings 2: Australia (142/4 in 15.2 overs - exciting chase!)
        Innings inn2 = new Innings(match.getId(), 2, australia, india, 187);
        inn2.setTotalRuns(142);
        inn2.setTotalWickets(4);
        inn2.setLegalBalls(92); // 15.2 overs
        inn2.setCompleted(false);
        inn2.setExtrasTotal(8);
        inn2.setWides(5);
        inn2.setNoBalls(1);
        inn2.setByes(1);
        inn2.setLegByes(1);

        Player maxwell = ausSquad.stream().filter(p -> p.getName().contains("Maxwell")).findFirst().orElse(ausSquad.get(3));
        Player stoinis = ausSquad.stream().filter(p -> p.getName().contains("Stoinis")).findFirst().orElse(ausSquad.get(4));
        Player bumrah = indSquad.stream().filter(p -> p.getName().contains("Bumrah")).findFirst().orElse(indSquad.get(8));

        inn2.setStrikerPlayerId(maxwell.getId());
        inn2.setNonStrikerPlayerId(stoinis.getId());
        inn2.setCurrentBowlerPlayerId(bumrah.getId());
        inn2 = inningsRepository.save(inn2);

        // Innings 2 Batting
        createAusBattingScorecard(inn2, ausSquad, maxwell, stoinis);
        // Innings 2 Bowling
        createIndBowlingScorecard(inn2, indSquad, bumrah);

        // Add historical ball events for Innings 2 recent overs
        seedRecentBallEvents(match.getId(), inn2.getId(), maxwell, stoinis, bumrah);

        return match;
    }

    private void createIndianBattingScorecard(Innings inn1, List<Player> ind) {
        BattingScorecard rohit = new BattingScorecard(inn1.getId(), ind.get(0), 1);
        rohit.setRuns(45); rohit.setBallsFaced(28); rohit.setFours(4); rohit.setSixes(3);
        rohit.setOut(true); rohit.setDismissalType(WicketType.CAUGHT); rohit.setDismissalInfo("c Warner b Cummins");
        battingScorecardRepository.save(rohit);

        BattingScorecard kohli = new BattingScorecard(inn1.getId(), ind.get(1), 2);
        kohli.setRuns(68); kohli.setBallsFaced(44); kohli.setFours(7); kohli.setSixes(2);
        kohli.setOut(false); kohli.setDismissalInfo("not out");
        battingScorecardRepository.save(kohli);

        BattingScorecard sky = new BattingScorecard(inn1.getId(), ind.get(2), 3);
        sky.setRuns(31); sky.setBallsFaced(16); sky.setFours(3); sky.setSixes(2);
        sky.setOut(true); sky.setDismissalType(WicketType.CAUGHT); sky.setDismissalInfo("c Wade b Starc");
        battingScorecardRepository.save(sky);

        BattingScorecard pant = new BattingScorecard(inn1.getId(), ind.get(3), 4);
        pant.setRuns(15); pant.setBallsFaced(12); pant.setFours(1); pant.setSixes(0);
        pant.setOut(true); pant.setDismissalType(WicketType.BOWLED); pant.setDismissalInfo("b Zampa");
        battingScorecardRepository.save(pant);

        BattingScorecard hardik = new BattingScorecard(inn1.getId(), ind.get(4), 5);
        hardik.setRuns(21); hardik.setBallsFaced(14); hardik.setFours(2); hardik.setSixes(1);
        hardik.setOut(true); hardik.setDismissalType(WicketType.CAUGHT); hardik.setDismissalInfo("c Head b Hazlewood");
        battingScorecardRepository.save(hardik);

        for (int i = 5; i < ind.size(); i++) {
            BattingScorecard b = new BattingScorecard(inn1.getId(), ind.get(i), i + 1);
            battingScorecardRepository.save(b);
        }
    }

    private void createAusBowlingScorecard(Innings inn1, List<Player> aus) {
        for (Player p : aus) {
            BowlingScorecard b = new BowlingScorecard(inn1.getId(), p);
            if (p.getName().contains("Starc")) {
                b.setLegalBalls(24); b.setRunsConceded(42); b.setWicketsTaken(1); b.setDotBalls(8);
            } else if (p.getName().contains("Hazlewood")) {
                b.setLegalBalls(24); b.setRunsConceded(32); b.setWicketsTaken(1); b.setDotBalls(11);
            } else if (p.getName().contains("Cummins")) {
                b.setLegalBalls(24); b.setRunsConceded(38); b.setWicketsTaken(1); b.setDotBalls(9);
            } else if (p.getName().contains("Zampa")) {
                b.setLegalBalls(24); b.setRunsConceded(36); b.setWicketsTaken(1); b.setDotBalls(7);
            } else if (p.getName().contains("Stoinis")) {
                b.setLegalBalls(24); b.setRunsConceded(38); b.setWicketsTaken(0); b.setDotBalls(6);
            }
            battingScorecardRepository.flush();
            bowlingScorecardRepository.save(b);
        }
    }

    private void createAusBattingScorecard(Innings inn2, List<Player> aus, Player maxwell, Player stoinis) {
        Player head = aus.get(0);
        BattingScorecard headSc = new BattingScorecard(inn2.getId(), head, 1);
        headSc.setRuns(52); headSc.setBallsFaced(30); headSc.setFours(6); headSc.setSixes(2);
        headSc.setOut(true); headSc.setDismissalType(WicketType.BOWLED); headSc.setDismissalInfo("b Bumrah");
        battingScorecardRepository.save(headSc);

        Player warner = aus.get(1);
        BattingScorecard warnerSc = new BattingScorecard(inn2.getId(), warner, 2);
        warnerSc.setRuns(18); warnerSc.setBallsFaced(12); warnerSc.setFours(2); warnerSc.setSixes(1);
        warnerSc.setOut(true); warnerSc.setDismissalType(WicketType.CAUGHT); warnerSc.setDismissalInfo("c Kohli b Arshdeep");
        battingScorecardRepository.save(warnerSc);

        Player marsh = aus.get(2);
        BattingScorecard marshSc = new BattingScorecard(inn2.getId(), marsh, 3);
        marshSc.setRuns(24); marshSc.setBallsFaced(18); marshSc.setFours(3); marshSc.setSixes(0);
        marshSc.setOut(true); marshSc.setDismissalType(WicketType.CAUGHT); marshSc.setDismissalInfo("c Pant b Kuldeep");
        battingScorecardRepository.save(marshSc);

        BattingScorecard maxiSc = new BattingScorecard(inn2.getId(), maxwell, 4);
        maxiSc.setRuns(34); maxiSc.setBallsFaced(20); maxiSc.setFours(3); maxiSc.setSixes(2);
        maxiSc.setStriker(true); maxiSc.setOut(false); maxiSc.setDismissalInfo("not out");
        battingScorecardRepository.save(maxiSc);

        BattingScorecard stoinisSc = new BattingScorecard(inn2.getId(), stoinis, 5);
        stoinisSc.setRuns(12); stoinisSc.setBallsFaced(8); stoinisSc.setFours(1); stoinisSc.setSixes(0);
        stoinisSc.setNonStriker(true); stoinisSc.setOut(false); stoinisSc.setDismissalInfo("not out");
        battingScorecardRepository.save(stoinisSc);

        for (int i = 5; i < aus.size(); i++) {
            BattingScorecard b = new BattingScorecard(inn2.getId(), aus.get(i), i + 1);
            battingScorecardRepository.save(b);
        }
    }

    private void createIndBowlingScorecard(Innings inn2, List<Player> ind, Player bumrah) {
        for (Player p : ind) {
            BowlingScorecard b = new BowlingScorecard(inn2.getId(), p);
            if (p.getName().contains("Bumrah")) {
                b.setLegalBalls(20); b.setRunsConceded(21); b.setWicketsTaken(2); b.setDotBalls(12);
                b.setCurrentBowler(true);
            } else if (p.getName().contains("Arshdeep")) {
                b.setLegalBalls(24); b.setRunsConceded(38); b.setWicketsTaken(1); b.setDotBalls(8);
            } else if (p.getName().contains("Kuldeep")) {
                b.setLegalBalls(24); b.setRunsConceded(34); b.setWicketsTaken(1); b.setDotBalls(7);
            } else if (p.getName().contains("Hardik")) {
                b.setLegalBalls(18); b.setRunsConceded(32); b.setWicketsTaken(0); b.setDotBalls(4);
            } else if (p.getName().contains("Axar")) {
                b.setLegalBalls(6); b.setRunsConceded(14); b.setWicketsTaken(0); b.setDotBalls(1);
            }
            bowlingScorecardRepository.save(b);
        }
    }

    private void seedRecentBallEvents(Long matchId, Long inningsId, Player striker, Player nonStriker, Player bowler) {
        // Last 6 balls: 1, 4, 1, 6, 0, 2
        String[] displays = {"1", "4", "1", "6", "0", "2"};
        int[] runs = {1, 4, 1, 6, 0, 2};
        String[] comments = {
                "Arshdeep to Maxwell, 1 run. Pushed into deep cover for an easy single.",
                "Arshdeep to Stoinis, FOUR! Powerful strike hammered past mid-off to the fence!",
                "Arshdeep to Stoinis, 1 run. Dropped with soft hands to short third man.",
                "Arshdeep to Maxwell, SIX! REVERSE SWEEP OVER POINT! Astonishing innovation into the stands!",
                "Bumrah to Maxwell, no run. 145kph searing yorker, expertly blocked out.",
                "Bumrah to Maxwell, 2 runs. Clipped off the pads towards deep midwicket, hurry back for two."
        };

        for (int i = 0; i < displays.length; i++) {
            BallEvent be = new BallEvent();
            be.setMatchId(matchId);
            be.setInningsId(inningsId);
            be.setOverNumber(14 + (i / 4));
            be.setBallNumberInOver((i % 6) + 1);
            be.setTotalLegalBallsAtPoint(87 + i);
            be.setRunsScored(runs[i]);
            be.setBatsman(i % 2 == 0 ? striker : nonStriker);
            be.setBowler(bowler);
            be.setNonStriker(i % 2 == 0 ? nonStriker : striker);
            be.setBallDisplay(displays[i]);
            be.setCommentaryText(comments[i]);
            be.setScoreAfterBall(128 + (i * 2) + runs[i]);
            be.setWicketsAfterBall(4);
            be.setTimestamp(LocalDateTime.now().minusSeconds((displays.length - i) * 15L));
            ballEventRepository.save(be);
        }
    }

    private void createEngVsSAMatch(Team eng, Team sa, List<Player> engSquad, List<Player> saSquad) {
        Match m = new Match();
        m.setTitle("ICC Men's T20 Championship: England vs South Africa");
        m.setMatchType(MatchType.T20);
        m.setStatus(MatchStatus.LIVE);
        m.setVenue("Lord's Cricket Ground");
        m.setCity("London");
        m.setTeam1(eng);
        m.setTeam2(sa);
        m.setTossWinner(sa);
        m.setTossDecision(TossDecision.BAT);
        m.setTotalOversLimit(20);
        m.setCurrentInningsNumber(1);
        m.setSimulating(false);
        m.setSimulationSpeedMs(3000);
        m = matchRepository.save(m);

        Innings inn = new Innings(m.getId(), 1, sa, eng, null);
        inn.setTotalRuns(128);
        inn.setTotalWickets(2);
        inn.setLegalBalls(78); // 13.0 overs
        inn.setStrikerPlayerId(saSquad.get(2).getId()); // Markram
        inn.setNonStrikerPlayerId(saSquad.get(3).getId()); // Klaasen
        inn.setCurrentBowlerPlayerId(engSquad.get(5).getId()); // Archer
        inn = inningsRepository.save(inn);

        // Add batsmen and bowlers
        for (int i = 0; i < saSquad.size(); i++) {
            BattingScorecard bsc = new BattingScorecard(inn.getId(), saSquad.get(i), i + 1);
            if (i == 0) { bsc.setRuns(48); bsc.setBallsFaced(28); bsc.setOut(true); }
            else if (i == 1) { bsc.setRuns(14); bsc.setBallsFaced(12); bsc.setOut(true); }
            else if (i == 2) { bsc.setRuns(38); bsc.setBallsFaced(22); bsc.setStriker(true); }
            else if (i == 3) { bsc.setRuns(22); bsc.setBallsFaced(16); bsc.setNonStriker(true); }
            battingScorecardRepository.save(bsc);
        }
        for (Player p : engSquad) {
            BowlingScorecard bc = new BowlingScorecard(inn.getId(), p);
            if (p.getName().contains("Archer")) bc.setCurrentBowler(true);
            bowlingScorecardRepository.save(bc);
        }
    }

    private void createPakVsNZMatch(Team pak, Team nz) {
        Match m = new Match();
        m.setTitle("Tri-Series Final: Pakistan vs New Zealand");
        m.setMatchType(MatchType.T20);
        m.setStatus(MatchStatus.COMPLETED);
        m.setVenue("Eden Park");
        m.setCity("Auckland");
        m.setTeam1(nz);
        m.setTeam2(pak);
        m.setTossWinner(nz);
        m.setTossDecision(TossDecision.BAT);
        m.setTotalOversLimit(20);
        m.setCurrentInningsNumber(2);
        m.setTarget(178);
        m.setWinner(nz);
        m.setWinMargin("New Zealand won by 18 runs");
        m.setManOfTheMatch("Kane Williamson");
        m.setSimulating(false);
        m = matchRepository.save(m);

        Innings inn1 = new Innings(m.getId(), 1, nz, pak, null);
        inn1.setTotalRuns(177); inn1.setTotalWickets(6); inn1.setLegalBalls(120); inn1.setCompleted(true);
        inningsRepository.save(inn1);

        Innings inn2 = new Innings(m.getId(), 2, pak, nz, 178);
        inn2.setTotalRuns(159); inn2.setTotalWickets(8); inn2.setLegalBalls(120); inn2.setCompleted(true);
        inningsRepository.save(inn2);
    }

    private void createCskVsMiMatch(Team csk, Team mi) {
        Match m = new Match();
        m.setTitle("Indian Premier League: Chennai Super Kings vs Mumbai Indians");
        m.setMatchType(MatchType.T20);
        m.setStatus(MatchStatus.SCHEDULED);
        m.setVenue("Wankhede Stadium");
        m.setCity("Mumbai");
        m.setTeam1(mi);
        m.setTeam2(csk);
        m.setTotalOversLimit(20);
        m.setCurrentInningsNumber(1);
        m.setSimulating(false);
        matchRepository.save(m);
    }
}
