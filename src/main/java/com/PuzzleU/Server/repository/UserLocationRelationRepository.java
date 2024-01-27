package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.relations.UserLocationRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLocationRelationRepository extends JpaRepository<UserLocationRelation, Long> {
}
