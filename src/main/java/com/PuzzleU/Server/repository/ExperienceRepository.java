package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.experience.Experience;
import com.PuzzleU.Server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    Optional<Experience> findByExperienceIdAndUser(Long experienceId, User user);
    Optional<Experience> findByExperienceId(Long experienceId);
}
