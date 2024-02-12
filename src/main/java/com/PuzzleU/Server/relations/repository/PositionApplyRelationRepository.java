package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.relations.entity.PositionApplyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionApplyRelationRepository extends JpaRepository<PositionApplyRelation, Long> {
    List<PositionApplyRelation> findByApply(Apply apply);
}
