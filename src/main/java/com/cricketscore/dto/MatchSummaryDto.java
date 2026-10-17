package com.cricketscore.dto;

import java.util.ArrayList;
import java.util.List;

public class MatchSummaryDto {
    private Long matchId;
    private String title;
    private String status;
    private String resultText;
    private String manOfTheMatch;

    private String team1Name;
    private String team1Score;
    private String team1Overs;

    private String team2Name;
    private String team2Score;
    private String team2Overs;

    // Top performers
    private String topBatter1;
    private String topBatter1Stats;
    private String topBowler1;
    private String topBowler1Stats;

    private String topBatter2;
    private String topBatter2Stats;
    private String topBowler2;
    private String topBowler2Stats;

    // Progression data for charts
    private List<OverRunProgression> innings1Progression = new ArrayList<>();
    private List<OverRunProgression> innings2Progression = new ArrayList<>();

    public static class OverRunProgression {
        private int overNumber;
        private int cumulativeRuns;
        private int wickets;

        public OverRunProgression(int overNumber, int cumulativeRuns, int wickets) {
            this.overNumber = overNumber;
            this.cumulativeRuns = cumulativeRuns;
            this.wickets = wickets;
        }

        public int getOverNumber() {
            return overNumber;
        }

        public int getCumulativeRuns() {
            return cumulativeRuns;
        }

        public int getWickets() {
            return wickets;
        }
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public String getManOfTheMatch() {
        return manOfTheMatch;
    }

    public void setManOfTheMatch(String manOfTheMatch) {
        this.manOfTheMatch = manOfTheMatch;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(String team1Score) {
        this.team1Score = team1Score;
    }

    public String getTeam1Overs() {
        return team1Overs;
    }

    public void setTeam1Overs(String team1Overs) {
        this.team1Overs = team1Overs;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(String team2Score) {
        this.team2Score = team2Score;
    }

    public String getTeam2Overs() {
        return team2Overs;
    }

    public void setTeam2Overs(String team2Overs) {
        this.team2Overs = team2Overs;
    }

    public String getTopBatter1() {
        return topBatter1;
    }

    public void setTopBatter1(String topBatter1) {
        this.topBatter1 = topBatter1;
    }

    public String getTopBatter1Stats() {
        return topBatter1Stats;
    }

    public void setTopBatter1Stats(String topBatter1Stats) {
        this.topBatter1Stats = topBatter1Stats;
    }

    public String getTopBowler1() {
        return topBowler1;
    }

    public void setTopBowler1(String topBowler1) {
        this.topBowler1 = topBowler1;
    }

    public String getTopBowler1Stats() {
        return topBowler1Stats;
    }

    public void setTopBowler1Stats(String topBowler1Stats) {
        this.topBowler1Stats = topBowler1Stats;
    }

    public String getTopBatter2() {
        return topBatter2;
    }

    public void setTopBatter2(String topBatter2) {
        this.topBatter2 = topBatter2;
    }

    public String getTopBatter2Stats() {
        return topBatter2Stats;
    }

    public void setTopBatter2Stats(String topBatter2Stats) {
        this.topBatter2Stats = topBatter2Stats;
    }

    public String getTopBowler2() {
        return topBowler2;
    }

    public void setTopBowler2(String topBowler2) {
        this.topBowler2 = topBowler2;
    }

    public String getTopBowler2Stats() {
        return topBowler2Stats;
    }

    public void setTopBowler2Stats(String topBowler2Stats) {
        this.topBowler2Stats = topBowler2Stats;
    }

    public List<OverRunProgression> getInnings1Progression() {
        return innings1Progression;
    }

    public void setInnings1Progression(List<OverRunProgression> innings1Progression) {
        this.innings1Progression = innings1Progression;
    }

    public List<OverRunProgression> getInnings2Progression() {
        return innings2Progression;
    }

    public void setInnings2Progression(List<OverRunProgression> innings2Progression) {
        this.innings2Progression = innings2Progression;
    }
}
