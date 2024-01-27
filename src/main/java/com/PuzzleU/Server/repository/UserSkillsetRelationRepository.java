package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillsetRelationRepository extends JpaRepository<UserSkillsetRelation, Long> {
}
