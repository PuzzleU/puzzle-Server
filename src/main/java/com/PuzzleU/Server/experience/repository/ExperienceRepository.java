package com.PuzzleU.Server.experience.repository;

import com.PuzzleU.Server.experience.entity.Experience;
import com.PuzzleU.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    Optional<Experience> findByExperienceIdAndUser(Long experienceId, User user);
    Optional<Experience> findByExperienceId(Long experienceId);
    List<Experience> findByUser(User user);
}
