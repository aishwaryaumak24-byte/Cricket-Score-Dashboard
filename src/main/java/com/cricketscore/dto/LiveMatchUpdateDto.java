package com.cricketscore.dto;

import java.util.ArrayList;
import java.util.List;

public class LiveMatchUpdateDto {
    private Long matchId;
    private String title;
    private String status;
    private String matchType;
    private int currentInningsNumber;
    private int totalOversLimit;

    // Batting Team
    private String battingTeamName;
    private String battingTeamShort;
    private String battingTeamFlag;
    private String battingTeamColor;

    // Bowling Team
    private String bowlingTeamName;
    private String bowlingTeamShort;
    private String bowlingTeamFlag;
    private String bowlingTeamColor;

    // Score & Overs
    private int totalRuns;
    private int totalWickets;
    private String oversFormatted;
    private int legalBalls;
    private String currentScore; // "164/4"
    private double currentRunRate;
    private Double requiredRunRate;
    private int projectedScore;
    private Integer target;

    // Striker Batter
    private Long strikerId;
    private String strikerName;
    private int strikerRuns;
    private int strikerBalls;
    private int strikerFours;
    private int strikerSixes;
    private double strikerStrikeRate;

    // Non-Striker Batter
    private Long nonStrikerId;
    private String nonStrikerName;
    private int nonStrikerRuns;
    private int nonStrikerBalls;
    private double nonStrikerStrikeRate;

    // Current Bowler
    private Long bowlerId;
    private String bowlerName;
    private String bowlerOvers;
    private int bowlerRunsConceded;
    private int bowlerWickets;
    private double bowlerEconomy;

    // Recent events
    private List<String> recentBalls = new ArrayList<>();
    private String latestCommentary;
    private String latestBallDisplay;

    // Status / Result
    private String winMargin;
    private String winnerTeamName;
    private boolean isSimulating;

    public LiveMatchUpdateDto() {
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

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public int getCurrentInningsNumber() {
        return currentInningsNumber;
    }

    public void setCurrentInningsNumber(int currentInningsNumber) {
        this.currentInningsNumber = currentInningsNumber;
    }

    public int getTotalOversLimit() {
        return totalOversLimit;
    }

    public void setTotalOversLimit(int totalOversLimit) {
        this.totalOversLimit = totalOversLimit;
    }

    public String getBattingTeamName() {
        return battingTeamName;
    }

    public void setBattingTeamName(String battingTeamName) {
        this.battingTeamName = battingTeamName;
    }

    public String getBattingTeamShort() {
        return battingTeamShort;
    }

    public void setBattingTeamShort(String battingTeamShort) {
        this.battingTeamShort = battingTeamShort;
    }

    public String getBattingTeamFlag() {
        return battingTeamFlag;
    }

    public void setBattingTeamFlag(String battingTeamFlag) {
        this.battingTeamFlag = battingTeamFlag;
    }

    public String getBattingTeamColor() {
        return battingTeamColor;
    }

    public void setBattingTeamColor(String battingTeamColor) {
        this.battingTeamColor = battingTeamColor;
    }

    public String getBowlingTeamName() {
        return bowlingTeamName;
    }

    public void setBowlingTeamName(String bowlingTeamName) {
        this.bowlingTeamName = bowlingTeamName;
    }

    public String getBowlingTeamShort() {
        return bowlingTeamShort;
    }

    public void setBowlingTeamShort(String bowlingTeamShort) {
        this.bowlingTeamShort = bowlingTeamShort;
    }

    public String getBowlingTeamFlag() {
        return bowlingTeamFlag;
    }

    public void setBowlingTeamFlag(String bowlingTeamFlag) {
        this.bowlingTeamFlag = bowlingTeamFlag;
    }

    public String getBowlingTeamColor() {
        return bowlingTeamColor;
    }

    public void setBowlingTeamColor(String bowlingTeamColor) {
        this.bowlingTeamColor = bowlingTeamColor;
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

    public int getLegalBalls() {
        return legalBalls;
    }

    public void setLegalBalls(int legalBalls) {
        this.legalBalls = legalBalls;
    }

    public String getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(String currentScore) {
        this.currentScore = currentScore;
    }

    public double getCurrentRunRate() {
        return currentRunRate;
    }

    public void setCurrentRunRate(double currentRunRate) {
        this.currentRunRate = currentRunRate;
    }

    public Double getRequiredRunRate() {
        return requiredRunRate;
    }

    public void setRequiredRunRate(Double requiredRunRate) {
        this.requiredRunRate = requiredRunRate;
    }

    public int getProjectedScore() {
        return projectedScore;
    }

    public void setProjectedScore(int projectedScore) {
        this.projectedScore = projectedScore;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Long getStrikerId() {
        return strikerId;
    }

    public void setStrikerId(Long strikerId) {
        this.strikerId = strikerId;
    }

    public String getStrikerName() {
        return strikerName;
    }

    public void setStrikerName(String strikerName) {
        this.strikerName = strikerName;
    }

    public int getStrikerRuns() {
        return strikerRuns;
    }

    public void setStrikerRuns(int strikerRuns) {
        this.strikerRuns = strikerRuns;
    }

    public int getStrikerBalls() {
        return strikerBalls;
    }

    public void setStrikerBalls(int strikerBalls) {
        this.strikerBalls = strikerBalls;
    }

    public int getStrikerFours() {
        return strikerFours;
    }

    public void setStrikerFours(int strikerFours) {
        this.strikerFours = strikerFours;
    }

    public int getStrikerSixes() {
        return strikerSixes;
    }

    public void setStrikerSixes(int strikerSixes) {
        this.strikerSixes = strikerSixes;
    }

    public double getStrikerStrikeRate() {
        return strikerStrikeRate;
    }

    public void setStrikerStrikeRate(double strikerStrikeRate) {
        this.strikerStrikeRate = strikerStrikeRate;
    }

    public Long getNonStrikerId() {
        return nonStrikerId;
    }

    public void setNonStrikerId(Long nonStrikerId) {
        this.nonStrikerId = nonStrikerId;
    }

    public String getNonStrikerName() {
        return nonStrikerName;
    }

    public void setNonStrikerName(String nonStrikerName) {
        this.nonStrikerName = nonStrikerName;
    }

    public int getNonStrikerRuns() {
        return nonStrikerRuns;
    }

    public void setNonStrikerRuns(int nonStrikerRuns) {
        this.nonStrikerRuns = nonStrikerRuns;
    }

    public int getNonStrikerBalls() {
        return nonStrikerBalls;
    }

    public void setNonStrikerBalls(int nonStrikerBalls) {
        this.nonStrikerBalls = nonStrikerBalls;
    }

    public double getNonStrikerStrikeRate() {
        return nonStrikerStrikeRate;
    }

    public void setNonStrikerStrikeRate(double nonStrikerStrikeRate) {
        this.nonStrikerStrikeRate = nonStrikerStrikeRate;
    }

    public Long getBowlerId() {
        return bowlerId;
    }

    public void setBowlerId(Long bowlerId) {
        this.bowlerId = bowlerId;
    }

    public String getBowlerName() {
        return bowlerName;
    }

    public void setBowlerName(String bowlerName) {
        this.bowlerName = bowlerName;
    }

    public String getBowlerOvers() {
        return bowlerOvers;
    }

    public void setBowlerOvers(String bowlerOvers) {
        this.bowlerOvers = bowlerOvers;
    }

    public int getBowlerRunsConceded() {
        return bowlerRunsConceded;
    }

    public void setBowlerRunsConceded(int bowlerRunsConceded) {
        this.bowlerRunsConceded = bowlerRunsConceded;
    }

    public int getBowlerWickets() {
        return bowlerWickets;
    }

    public void setBowlerWickets(int bowlerWickets) {
        this.bowlerWickets = bowlerWickets;
    }

    public double getBowlerEconomy() {
        return bowlerEconomy;
    }

    public void setBowlerEconomy(double bowlerEconomy) {
        this.bowlerEconomy = bowlerEconomy;
    }

    public List<String> getRecentBalls() {
        return recentBalls;
    }

    public void setRecentBalls(List<String> recentBalls) {
        this.recentBalls = recentBalls;
    }

    public String getLatestCommentary() {
        return latestCommentary;
    }

    public void setLatestCommentary(String latestCommentary) {
        this.latestCommentary = latestCommentary;
    }

    public String getLatestBallDisplay() {
        return latestBallDisplay;
    }

    public void setLatestBallDisplay(String latestBallDisplay) {
        this.latestBallDisplay = latestBallDisplay;
    }

    public String getWinMargin() {
        return winMargin;
    }

    public void setWinMargin(String winMargin) {
        this.winMargin = winMargin;
    }

    public String getWinnerTeamName() {
        return winnerTeamName;
    }

    public void setWinnerTeamName(String winnerTeamName) {
        this.winnerTeamName = winnerTeamName;
    }

    public boolean isSimulating() {
        return isSimulating;
    }

    public void setSimulating(boolean simulating) {
        isSimulating = simulating;
    }
}
