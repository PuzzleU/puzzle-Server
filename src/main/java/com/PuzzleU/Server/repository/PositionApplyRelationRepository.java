package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.relations.PositionApplyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionApplyRelationRepository extends JpaRepository<PositionApplyRelation, Long> {
}
