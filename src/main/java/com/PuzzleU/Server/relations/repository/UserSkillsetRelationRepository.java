package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.relations.entity.UserSkillsetRelation;
import com.PuzzleU.Server.skillset.entity.Skillset;
import com.PuzzleU.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillsetRelationRepository extends JpaRepository<UserSkillsetRelation, Long> {
    Optional<UserSkillsetRelation> findByUserAndSkillset(User user, Skillset skillset);
    List<UserSkillsetRelation> findByUser(User user);
}
