package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.relations.UserPositionRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPositionRelationRepository extends JpaRepository<UserPositionRelation, Long> {
}
