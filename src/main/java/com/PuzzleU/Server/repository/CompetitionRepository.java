package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.competition.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {

}
