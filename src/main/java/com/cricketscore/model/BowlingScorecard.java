package com.cricketscore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bowling_scorecards")
public class BowlingScorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long inningsId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    private int legalBalls;
    private int maidenOvers;
    private int runsConceded;
    private int wicketsTaken;
    private int wides;
    private int noBalls;
    private int dotBalls;

    private boolean isCurrentBowler;

    public BowlingScorecard() {
        this.legalBalls = 0;
        this.maidenOvers = 0;
        this.runsConceded = 0;
        this.wicketsTaken = 0;
        this.wides = 0;
        this.noBalls = 0;
        this.dotBalls = 0;
        this.isCurrentBowler = false;
    }

    public BowlingScorecard(Long inningsId, Player player) {
        this();
        this.inningsId = inningsId;
        this.player = player;
    }

    public String getOversFormatted() {
        int overs = legalBalls / 6;
        int balls = legalBalls % 6;
        return overs + "." + balls;
    }

    public double getOversAsDouble() {
        return (legalBalls / 6) + ((legalBalls % 6) / 6.0);
    }

    public double getEconomyRate() {
        double overs = getOversAsDouble();
        if (overs == 0) return 0.0;
        return Math.round((runsConceded / overs) * 100.0) / 100.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInningsId() {
        return inningsId;
    }

    public void setInningsId(Long inningsId) {
        this.inningsId = inningsId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getLegalBalls() {
        return legalBalls;
    }

    public void setLegalBalls(int legalBalls) {
        this.legalBalls = legalBalls;
    }

    public int getMaidenOvers() {
        return maidenOvers;
    }

    public void setMaidenOvers(int maidenOvers) {
        this.maidenOvers = maidenOvers;
    }

    public int getRunsConceded() {
        return runsConceded;
    }

    public void setRunsConceded(int runsConceded) {
        this.runsConceded = runsConceded;
    }

    public int getWicketsTaken() {
        return wicketsTaken;
    }

    public void setWicketsTaken(int wicketsTaken) {
        this.wicketsTaken = wicketsTaken;
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

    public int getDotBalls() {
        return dotBalls;
    }

    public void setDotBalls(int dotBalls) {
        this.dotBalls = dotBalls;
    }

    public boolean isCurrentBowler() {
        return isCurrentBowler;
    }

    public void setCurrentBowler(boolean currentBowler) {
        isCurrentBowler = currentBowler;
    }
}
