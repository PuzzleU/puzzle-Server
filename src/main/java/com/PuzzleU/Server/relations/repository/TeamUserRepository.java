package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.relations.entity.TeamUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamUserRepository extends JpaRepository<TeamUserRelation, Long> {
}
