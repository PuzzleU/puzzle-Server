package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import com.PuzzleU.Server.entity.skillset.Skillset;
import com.PuzzleU.Server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillsetRelationRepository extends JpaRepository<UserSkillsetRelation, Long> {
    Optional<UserSkillsetRelation> findByUserAndSkillset(User user, Skillset skillset);
    List<UserSkillsetRelation> findByUser(User user);
}
