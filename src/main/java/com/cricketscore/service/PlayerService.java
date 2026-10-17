package com.cricketscore.service;

import com.cricketscore.model.Player;
import com.cricketscore.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public List<Player> getPlayersByTeamId(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }

    public Map<String, Object> getLeaderboard() {
        Map<String, Object> leaderboard = new HashMap<>();
        List<Player> topBatsmen = playerRepository.findTop10ByOrderByTotalRunsDesc();
        List<Player> topBowlers = playerRepository.findTop10ByOrderByTotalWicketsDesc();

        leaderboard.put("topRunScorers", topBatsmen);
        leaderboard.put("topWicketTakers", topBowlers);
        return leaderboard;
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
}
