package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.relations.TeamUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamUserRepository extends JpaRepository<TeamUserRelation, Long> {
}
