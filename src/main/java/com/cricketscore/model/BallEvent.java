package com.cricketscore.model;

import com.cricketscore.model.enums.ExtraType;
import com.cricketscore.model.enums.WicketType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ball_events")
public class BallEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long matchId;

    @Column(nullable = false)
    private Long inningsId;

    private int overNumber; // 0 to 19 (for 20 overs)
    private int ballNumberInOver; // 1 to 6
    private int totalLegalBallsAtPoint;

    private int runsScored; // runs scored off the bat

    @Enumerated(EnumType.STRING)
    private ExtraType extraType;
    private int extraRuns;

    private boolean isWicket;

    @Enumerated(EnumType.STRING)
    private WicketType wicketType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batsman_id")
    private Player batsman;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bowler_id")
    private Player bowler;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "non_striker_id")
    private Player nonStriker;

    @Column(length = 1000)
    private String commentaryText;

    private String ballDisplay; // e.g. "0", "1", "4", "6", "W", "1wd", "4nb"

    private int scoreAfterBall;
    private int wicketsAfterBall;

    private LocalDateTime timestamp;

    public BallEvent() {
        this.extraType = ExtraType.NONE;
        this.wicketType = WicketType.NONE;
        this.timestamp = LocalDateTime.now();
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

    public Long getInningsId() {
        return inningsId;
    }

    public void setInningsId(Long inningsId) {
        this.inningsId = inningsId;
    }

    public int getOverNumber() {
        return overNumber;
    }

    public void setOverNumber(int overNumber) {
        this.overNumber = overNumber;
    }

    public int getBallNumberInOver() {
        return ballNumberInOver;
    }

    public void setBallNumberInOver(int ballNumberInOver) {
        this.ballNumberInOver = ballNumberInOver;
    }

    public int getTotalLegalBallsAtPoint() {
        return totalLegalBallsAtPoint;
    }

    public void setTotalLegalBallsAtPoint(int totalLegalBallsAtPoint) {
        this.totalLegalBallsAtPoint = totalLegalBallsAtPoint;
    }

    public int getRunsScored() {
        return runsScored;
    }

    public void setRunsScored(int runsScored) {
        this.runsScored = runsScored;
    }

    public ExtraType getExtraType() {
        return extraType;
    }

    public void setExtraType(ExtraType extraType) {
        this.extraType = extraType;
    }

    public int getExtraRuns() {
        return extraRuns;
    }

    public void setExtraRuns(int extraRuns) {
        this.extraRuns = extraRuns;
    }

    public boolean isWicket() {
        return isWicket;
    }

    public void setWicket(boolean wicket) {
        isWicket = wicket;
    }

    public WicketType getWicketType() {
        return wicketType;
    }

    public void setWicketType(WicketType wicketType) {
        this.wicketType = wicketType;
    }

    public Player getBatsman() {
        return batsman;
    }

    public void setBatsman(Player batsman) {
        this.batsman = batsman;
    }

    public Player getBowler() {
        return bowler;
    }

    public void setBowler(Player bowler) {
        this.bowler = bowler;
    }

    public Player getNonStriker() {
        return nonStriker;
    }

    public void setNonStriker(Player nonStriker) {
        this.nonStriker = nonStriker;
    }

    public String getCommentaryText() {
        return commentaryText;
    }

    public void setCommentaryText(String commentaryText) {
        this.commentaryText = commentaryText;
    }

    public String getBallDisplay() {
        return ballDisplay;
    }

    public void setBallDisplay(String ballDisplay) {
        this.ballDisplay = ballDisplay;
    }

    public int getScoreAfterBall() {
        return scoreAfterBall;
    }

    public void setScoreAfterBall(int scoreAfterBall) {
        this.scoreAfterBall = scoreAfterBall;
    }

    public int getWicketsAfterBall() {
        return wicketsAfterBall;
    }

    public void setWicketsAfterBall(int wicketsAfterBall) {
        this.wicketsAfterBall = wicketsAfterBall;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
