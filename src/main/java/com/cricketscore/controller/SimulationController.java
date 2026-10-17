package com.cricketscore.controller;

import com.cricketscore.dto.LiveMatchUpdateDto;
import com.cricketscore.dto.ScoreBallRequest;
import com.cricketscore.service.MatchSimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches/{id}")
public class SimulationController {

    private final MatchSimulationService simulationService;

    public SimulationController(MatchSimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/start")
    public ResponseEntity<Void> startSimulation(
            @PathVariable Long id,
            @RequestParam(defaultValue = "2000") Long speed) {
        simulationService.startSimulation(id, speed);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pause")
    public ResponseEntity<Void> pauseSimulation(@PathVariable Long id) {
        simulationService.pauseSimulation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/step")
    public ResponseEntity<LiveMatchUpdateDto> simulateNextBall(@PathVariable Long id) {
        LiveMatchUpdateDto update = simulationService.simulateNextBall(id);
        return ResponseEntity.ok(update);
    }

    @PostMapping("/score-ball")
    public ResponseEntity<LiveMatchUpdateDto> scoreBall(
            @PathVariable Long id,
            @RequestBody ScoreBallRequest request) {
        LiveMatchUpdateDto update = simulationService.scoreBall(id, request);
        return ResponseEntity.ok(update);
    }
}
