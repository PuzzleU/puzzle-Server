package com.PuzzleU.Server.skillset.repository;

import com.PuzzleU.Server.skillset.entity.Skillset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsetRepository extends JpaRepository<Skillset, Long> {
}
