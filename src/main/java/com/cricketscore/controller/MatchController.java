package com.cricketscore.controller;

import com.cricketscore.dto.*;
import com.cricketscore.model.BallEvent;
import com.cricketscore.model.Match;
import com.cricketscore.model.enums.MatchStatus;
import com.cricketscore.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches(@RequestParam(required = false) MatchStatus status) {
        if (status != null) {
            return ResponseEntity.ok(matchService.getMatchesByStatus(status));
        }
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/live")
    public ResponseEntity<LiveMatchUpdateDto> getLiveMatchUpdate(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(matchService.getLiveUpdate(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/scorecard")
    public ResponseEntity<ScorecardResponseDto> getMatchScorecard(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(matchService.getMatchScorecard(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<MatchSummaryDto> getMatchSummary(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(matchService.getMatchSummary(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/commentary")
    public ResponseEntity<List<BallEvent>> getCommentary(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return ResponseEntity.ok(matchService.getBallCommentary(id, page, size));
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody CreateMatchRequest request) {
        return ResponseEntity.ok(matchService.createMatch(request));
    }

    @PostMapping("/{id}/reset")
    public ResponseEntity<Void> resetMatch(@PathVariable Long id) {
        matchService.resetMatch(id);
        return ResponseEntity.ok().build();
    }
}
