package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.skillset.Skillset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsetRepository extends JpaRepository<Skillset, Long> {
}
