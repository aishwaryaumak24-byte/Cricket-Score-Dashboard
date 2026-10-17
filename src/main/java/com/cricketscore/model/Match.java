package com.cricketscore.model;

import com.cricketscore.model.enums.MatchStatus;
import com.cricketscore.model.enums.MatchType;
import com.cricketscore.model.enums.TossDecision;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private String venue;
    private String city;
    private LocalDateTime matchDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team2_id")
    private Team team2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "toss_winner_id")
    private Team tossWinner;

    @Enumerated(EnumType.STRING)
    private TossDecision tossDecision;

    private int totalOversLimit; // e.g. 20 for T20, 50 for ODI
    private int currentInningsNumber; // 1 or 2

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "winner_id")
    private Team winner;

    private String winMargin;
    private String manOfTheMatch;

    private boolean isSimulating;
    private long simulationSpeedMs;
    private Integer target;

    public Match() {
        this.status = MatchStatus.SCHEDULED;
        this.matchType = MatchType.T20;
        this.totalOversLimit = 20;
        this.currentInningsNumber = 1;
        this.simulationSpeedMs = 2500;
        this.matchDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Team getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(Team tossWinner) {
        this.tossWinner = tossWinner;
    }

    public TossDecision getTossDecision() {
        return tossDecision;
    }

    public void setTossDecision(TossDecision tossDecision) {
        this.tossDecision = tossDecision;
    }

    public int getTotalOversLimit() {
        return totalOversLimit;
    }

    public void setTotalOversLimit(int totalOversLimit) {
        this.totalOversLimit = totalOversLimit;
    }

    public int getCurrentInningsNumber() {
        return currentInningsNumber;
    }

    public void setCurrentInningsNumber(int currentInningsNumber) {
        this.currentInningsNumber = currentInningsNumber;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public String getWinMargin() {
        return winMargin;
    }

    public void setWinMargin(String winMargin) {
        this.winMargin = winMargin;
    }

    public String getManOfTheMatch() {
        return manOfTheMatch;
    }

    public void setManOfTheMatch(String manOfTheMatch) {
        this.manOfTheMatch = manOfTheMatch;
    }

    public boolean isSimulating() {
        return isSimulating;
    }

    public void setSimulating(boolean simulating) {
        isSimulating = simulating;
    }

    public long getSimulationSpeedMs() {
        return simulationSpeedMs;
    }

    public void setSimulationSpeedMs(long simulationSpeedMs) {
        this.simulationSpeedMs = simulationSpeedMs;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
