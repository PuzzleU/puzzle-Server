package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.relations.entity.UserSkillsetRelation;
import com.PuzzleU.Server.skillset.entity.Skillset;
import com.PuzzleU.Server.user.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillsetRelationRepository extends JpaRepository<UserSkillsetRelation, Long> {
    Optional<UserSkillsetRelation> findByUserAndSkillset(User user, Skillset skillset);
    List<UserSkillsetRelation> findByUser(User user);

    @Query("Select u FROM UserSkillsetRelation u WHERE (u.user =:user And u.userSkillsetRelationId =:userSkillsetId)")
    Optional<UserSkillsetRelation> findByUserAndUserSkillsetId(@Param("user") User user, @Param("userSkillsetId") Long userSkillsetId);
}
