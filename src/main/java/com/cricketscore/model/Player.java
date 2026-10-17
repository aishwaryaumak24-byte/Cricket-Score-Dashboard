package com.cricketscore.model;

import com.cricketscore.model.enums.PlayerRole;
import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long teamId;
    private String teamName;

    @Enumerated(EnumType.STRING)
    private PlayerRole role;

    private String battingStyle;
    private String bowlingStyle;
    private int jerseyNumber;

    // Career / Tournament Stats
    private int matchesPlayed;
    private int totalRuns;
    private int highestScore;
    private int centuries;
    private int halfCenturies;
    private int totalWickets;
    private String bestBowlingFigures;
    private double careerEconomy;
    private double careerStrikeRate;

    public Player() {
    }

    public Player(String name, Long teamId, String teamName, PlayerRole role, String battingStyle, String bowlingStyle, int jerseyNumber) {
        this.name = name;
        this.teamId = teamId;
        this.teamName = teamName;
        this.role = role;
        this.battingStyle = battingStyle;
        this.bowlingStyle = bowlingStyle;
        this.jerseyNumber = jerseyNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public PlayerRole getRole() {
        return role;
    }

    public void setRole(PlayerRole role) {
        this.role = role;
    }

    public String getBattingStyle() {
        return battingStyle;
    }

    public void setBattingStyle(String battingStyle) {
        this.battingStyle = battingStyle;
    }

    public String getBowlingStyle() {
        return bowlingStyle;
    }

    public void setBowlingStyle(String bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getCenturies() {
        return centuries;
    }

    public void setCenturies(int centuries) {
        this.centuries = centuries;
    }

    public int getHalfCenturies() {
        return halfCenturies;
    }

    public void setHalfCenturies(int halfCenturies) {
        this.halfCenturies = halfCenturies;
    }

    public int getTotalWickets() {
        return totalWickets;
    }

    public void setTotalWickets(int totalWickets) {
        this.totalWickets = totalWickets;
    }

    public String getBestBowlingFigures() {
        return bestBowlingFigures;
    }

    public void setBestBowlingFigures(String bestBowlingFigures) {
        this.bestBowlingFigures = bestBowlingFigures;
    }

    public double getCareerEconomy() {
        return careerEconomy;
    }

    public void setCareerEconomy(double careerEconomy) {
        this.careerEconomy = careerEconomy;
    }

    public double getCareerStrikeRate() {
        return careerStrikeRate;
    }

    public void setCareerStrikeRate(double careerStrikeRate) {
        this.careerStrikeRate = careerStrikeRate;
    }
}
