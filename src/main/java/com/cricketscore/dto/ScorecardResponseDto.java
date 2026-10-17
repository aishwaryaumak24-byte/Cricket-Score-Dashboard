package com.cricketscore.dto;

import com.cricketscore.model.BattingScorecard;
import com.cricketscore.model.BowlingScorecard;
import com.cricketscore.model.Match;
import java.util.ArrayList;
import java.util.List;

public class ScorecardResponseDto {
    private Match match;
    private InningsScorecardDto innings1;
    private InningsScorecardDto innings2;

    public static class InningsScorecardDto {
        private Long inningsId;
        private int inningsNumber;
        private String teamName;
        private String teamShort;
        private String teamFlag;
        private int totalRuns;
        private int totalWickets;
        private String oversFormatted;
        private double runRate;
        private Integer target;

        private int extrasTotal;
        private int wides;
        private int noBalls;
        private int byes;
        private int legByes;

        private List<BattingScorecard> batsmen = new ArrayList<>();
        private List<BowlingScorecard> bowlers = new ArrayList<>();

        public Long getInningsId() {
            return inningsId;
        }

        public void setInningsId(Long inningsId) {
            this.inningsId = inningsId;
        }

        public int getInningsNumber() {
            return inningsNumber;
        }

        public void setInningsNumber(int inningsNumber) {
            this.inningsNumber = inningsNumber;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getTeamShort() {
            return teamShort;
        }

        public void setTeamShort(String teamShort) {
            this.teamShort = teamShort;
        }

        public String getTeamFlag() {
            return teamFlag;
        }

        public void setTeamFlag(String teamFlag) {
            this.teamFlag = teamFlag;
        }

        public int getTotalRuns() {
            return totalRuns;
        }

        public void setTotalRuns(int totalRuns) {
            this.totalRuns = totalRuns;
        }

        public int getTotalWickets() {
            return totalWickets;
        }

        public void setTotalWickets(int totalWickets) {
            this.totalWickets = totalWickets;
        }

        public String getOversFormatted() {
            return oversFormatted;
        }

        public void setOversFormatted(String oversFormatted) {
            this.oversFormatted = oversFormatted;
        }

        public double getRunRate() {
            return runRate;
        }

        public void setRunRate(double runRate) {
            this.runRate = runRate;
        }

        public Integer getTarget() {
            return target;
        }

        public void setTarget(Integer target) {
            this.target = target;
        }

        public int getExtrasTotal() {
            return extrasTotal;
        }

        public void setExtrasTotal(int extrasTotal) {
            this.extrasTotal = extrasTotal;
        }

        public int getWides() {
            return wides;
        }

        public void setWides(int wides) {
            this.wides = wides;
        }

        public int getNoBalls() {
            return noBalls;
        }

        public void setNoBalls(int noBalls) {
            this.noBalls = noBalls;
        }

        public int getByes() {
            return byes;
        }

        public void setByes(int byes) {
            this.byes = byes;
        }

        public int getLegByes() {
            return legByes;
        }

        public void setLegByes(int legByes) {
            this.legByes = legByes;
        }

        public List<BattingScorecard> getBatsmen() {
            return batsmen;
        }

        public void setBatsmen(List<BattingScorecard> batsmen) {
            this.batsmen = batsmen;
        }

        public List<BowlingScorecard> getBowlers() {
            return bowlers;
        }

        public void setBowlers(List<BowlingScorecard> bowlers) {
            this.bowlers = bowlers;
        }
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public InningsScorecardDto getInnings1() {
        return innings1;
    }

    public void setInnings1(InningsScorecardDto innings1) {
        this.innings1 = innings1;
    }

    public InningsScorecardDto getInnings2() {
        return innings2;
    }

    public void setInnings2(InningsScorecardDto innings2) {
        this.innings2 = innings2;
    }
}
