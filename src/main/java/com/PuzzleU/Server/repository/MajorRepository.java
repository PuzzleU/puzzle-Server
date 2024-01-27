package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.major.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
}
