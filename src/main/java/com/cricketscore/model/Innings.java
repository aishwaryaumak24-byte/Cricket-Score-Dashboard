package com.cricketscore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "innings")
public class Innings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long matchId;

    private int inningsNumber; // 1 or 2

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batting_team_id")
    private Team battingTeam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bowling_team_id")
    private Team bowlingTeam;

    private int totalRuns;
    private int totalWickets;
    private int legalBalls; // count of valid balls bowled

    private boolean isCompleted;

    // Extras
    private int extrasTotal;
    private int wides;
    private int noBalls;
    private int byes;
    private int legByes;
    private int penalties;

    // Current active players on field
    private Long strikerPlayerId;
    private Long nonStrikerPlayerId;
    private Long currentBowlerPlayerId;

    private Integer target;

    public Innings() {
        this.totalRuns = 0;
        this.totalWickets = 0;
        this.legalBalls = 0;
        this.isCompleted = false;
        this.extrasTotal = 0;
    }

    public Innings(Long matchId, int inningsNumber, Team battingTeam, Team bowlingTeam, Integer target) {
        this();
        this.matchId = matchId;
        this.inningsNumber = inningsNumber;
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.target = target;
    }

    public String getOversFormatted() {
        int overs = legalBalls / 6;
        int balls = legalBalls % 6;
        return overs + "." + balls;
    }

    public double getOversAsDouble() {
        return (legalBalls / 6) + ((legalBalls % 6) / 6.0);
    }

    public double getCurrentRunRate() {
        double overs = getOversAsDouble();
        if (overs == 0) return 0.0;
        return Math.round((totalRuns / overs) * 100.0) / 100.0;
    }

    public Double getRequiredRunRate(int maxOvers) {
        if (target == null) return null;
        int runsNeeded = target - totalRuns;
        int ballsRemaining = (maxOvers * 6) - legalBalls;
        if (ballsRemaining <= 0) return 0.0;
        double oversRemaining = ballsRemaining / 6.0;
        return Math.round((runsNeeded / oversRemaining) * 100.0) / 100.0;
    }

    public int getProjectedScore(int maxOvers) {
        double crr = getCurrentRunRate();
        if (crr == 0) return totalRuns;
        int ballsRemaining = (maxOvers * 6) - legalBalls;
        double additionalRuns = (crr / 6.0) * ballsRemaining;
        return (int) Math.round(totalRuns + additionalRuns);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public int getInningsNumber() {
        return inningsNumber;
    }

    public void setInningsNumber(int inningsNumber) {
        this.inningsNumber = inningsNumber;
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(Team battingTeam) {
        this.battingTeam = battingTeam;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }

    public void setBowlingTeam(Team bowlingTeam) {
        this.bowlingTeam = bowlingTeam;
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

    public int getLegalBalls() {
        return legalBalls;
    }

    public void setLegalBalls(int legalBalls) {
        this.legalBalls = legalBalls;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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

    public int getPenalties() {
        return penalties;
    }

    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }

    public Long getStrikerPlayerId() {
        return strikerPlayerId;
    }

    public void setStrikerPlayerId(Long strikerPlayerId) {
        this.strikerPlayerId = strikerPlayerId;
    }

    public Long getNonStrikerPlayerId() {
        return nonStrikerPlayerId;
    }

    public void setNonStrikerPlayerId(Long nonStrikerPlayerId) {
        this.nonStrikerPlayerId = nonStrikerPlayerId;
    }

    public Long getCurrentBowlerPlayerId() {
        return currentBowlerPlayerId;
    }

    public void setCurrentBowlerPlayerId(Long currentBowlerPlayerId) {
        this.currentBowlerPlayerId = currentBowlerPlayerId;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
