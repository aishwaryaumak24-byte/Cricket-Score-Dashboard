package com.cricketscore.repository;

import com.cricketscore.model.Match;
import com.cricketscore.model.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByStatus(MatchStatus status);
    List<Match> findByStatusOrderByMatchDateDesc(MatchStatus status);
    List<Match> findAllByOrderByMatchDateDesc();
}
