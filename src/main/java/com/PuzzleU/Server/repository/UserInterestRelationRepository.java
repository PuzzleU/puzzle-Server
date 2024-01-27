package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.relations.UserInterestRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRelationRepository extends JpaRepository<UserInterestRelation, Long> {
}
