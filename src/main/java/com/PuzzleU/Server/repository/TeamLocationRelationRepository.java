package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.relations.TeamLocationRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamLocationRelationRepository extends JpaRepository<TeamLocationRelation, Long> {
}
