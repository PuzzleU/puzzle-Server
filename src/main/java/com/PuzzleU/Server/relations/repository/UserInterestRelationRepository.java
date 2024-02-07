package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.relations.entity.UserInterestRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRelationRepository extends JpaRepository<UserInterestRelation, Long> {
}
