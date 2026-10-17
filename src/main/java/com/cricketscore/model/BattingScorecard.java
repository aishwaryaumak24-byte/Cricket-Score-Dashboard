package com.cricketscore.model;

import com.cricketscore.model.enums.WicketType;
import jakarta.persistence.*;

@Entity
@Table(name = "batting_scorecards")
public class BattingScorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long inningsId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    private int battingPosition;
    private int runs;
    private int ballsFaced;
    private int fours;
    private int sixes;

    private boolean isOut;

    @Enumerated(EnumType.STRING)
    private WicketType dismissalType;

    private String dismissalInfo; // e.g. "c Warner b Starc" or "not out"
    private String bowlerName;
    private String catcherName;

    private boolean isStriker;
    private boolean isNonStriker;

    public BattingScorecard() {
        this.runs = 0;
        this.ballsFaced = 0;
        this.fours = 0;
        this.sixes = 0;
        this.isOut = false;
        this.dismissalInfo = "not out";
        this.dismissalType = WicketType.NONE;
    }

    public BattingScorecard(Long inningsId, Player player, int battingPosition) {
        this();
        this.inningsId = inningsId;
        this.player = player;
        this.battingPosition = battingPosition;
    }

    public double getStrikeRate() {
        if (ballsFaced == 0) return 0.0;
        return Math.round(((double) runs / ballsFaced * 100.0) * 100.0) / 100.0;
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

    public int getBattingPosition() {
        return battingPosition;
    }

    public void setBattingPosition(int battingPosition) {
        this.battingPosition = battingPosition;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getBallsFaced() {
        return ballsFaced;
    }

    public void setBallsFaced(int ballsFaced) {
        this.ballsFaced = ballsFaced;
    }

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public WicketType getDismissalType() {
        return dismissalType;
    }

    public void setDismissalType(WicketType dismissalType) {
        this.dismissalType = dismissalType;
    }

    public String getDismissalInfo() {
        return dismissalInfo;
    }

    public void setDismissalInfo(String dismissalInfo) {
        this.dismissalInfo = dismissalInfo;
    }

    public String getBowlerName() {
        return bowlerName;
    }

    public void setBowlerName(String bowlerName) {
        this.bowlerName = bowlerName;
    }

    public String getCatcherName() {
        return catcherName;
    }

    public void setCatcherName(String catcherName) {
        this.catcherName = catcherName;
    }

    public boolean isStriker() {
        return isStriker;
    }

    public void setStriker(boolean striker) {
        isStriker = striker;
    }

    public boolean isNonStriker() {
        return isNonStriker;
    }

    public void setNonStriker(boolean nonStriker) {
        isNonStriker = nonStriker;
    }
}
