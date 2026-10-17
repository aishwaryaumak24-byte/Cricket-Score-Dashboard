package com.cricketscore.repository;

import com.cricketscore.model.BowlingScorecard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BowlingScorecardRepository extends JpaRepository<BowlingScorecard, Long> {
    List<BowlingScorecard> findByInningsId(Long inningsId);
    Optional<BowlingScorecard> findByInningsIdAndPlayerId(Long inningsId, Long playerId);
    Optional<BowlingScorecard> findByInningsIdAndIsCurrentBowlerTrue(Long inningsId);
}
