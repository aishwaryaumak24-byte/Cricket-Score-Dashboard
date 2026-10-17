package com.cricketscore.dto;

import com.cricketscore.model.enums.ExtraType;
import com.cricketscore.model.enums.WicketType;

public class ScoreBallRequest {
    private int runsScored; // 0, 1, 2, 3, 4, 6
    private ExtraType extraType = ExtraType.NONE;
    private int extraRuns;
    private boolean isWicket;
    private WicketType wicketType = WicketType.NONE;
    private String customCommentary;

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

    public String getCustomCommentary() {
        return customCommentary;
    }

    public void setCustomCommentary(String customCommentary) {
        this.customCommentary = customCommentary;
    }
}
