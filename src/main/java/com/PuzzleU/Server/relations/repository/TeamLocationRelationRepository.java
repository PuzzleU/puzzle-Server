package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.relations.entity.TeamLocationRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamLocationRelationRepository extends JpaRepository<TeamLocationRelation, Long> {
}
