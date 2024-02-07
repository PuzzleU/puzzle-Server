package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.relations.entity.UserLocationRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLocationRelationRepository extends JpaRepository<UserLocationRelation, Long> {
}
