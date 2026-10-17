package com.cricketscore.repository;

import com.cricketscore.model.BallEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BallEventRepository extends JpaRepository<BallEvent, Long> {
    List<BallEvent> findByInningsIdOrderByTotalLegalBallsAtPointAscIdAsc(Long inningsId);
    List<BallEvent> findByMatchIdOrderByIdDesc(Long matchId);
    List<BallEvent> findByInningsIdOrderByIdDesc(Long inningsId, Pageable pageable);
    List<BallEvent> findTop12ByInningsIdOrderByIdDesc(Long inningsId);
}
