package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.relations.entity.UserUniversityRelation;
import com.PuzzleU.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserUniversityRelationRepository extends JpaRepository<UserUniversityRelation, Long> {

    List<UserUniversityRelation> findByUser(User user);
}
