package com.cricketscore.dto;

import com.cricketscore.model.enums.MatchType;
import com.cricketscore.model.enums.TossDecision;

public class CreateMatchRequest {
    private String title;
    private Long team1Id;
    private Long team2Id;
    private Long tossWinnerId;
    private TossDecision tossDecision;
    private MatchType matchType = MatchType.T20;
    private int totalOversLimit = 20;
    private String venue;
    private String city;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Long team1Id) {
        this.team1Id = team1Id;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Long team2Id) {
        this.team2Id = team2Id;
    }

    public Long getTossWinnerId() {
        return tossWinnerId;
    }

    public void setTossWinnerId(Long tossWinnerId) {
        this.tossWinnerId = tossWinnerId;
    }

    public TossDecision getTossDecision() {
        return tossDecision;
    }

    public void setTossDecision(TossDecision tossDecision) {
        this.tossDecision = tossDecision;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public int getTotalOversLimit() {
        return totalOversLimit;
    }

    public void setTotalOversLimit(int totalOversLimit) {
        this.totalOversLimit = totalOversLimit;
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
}
