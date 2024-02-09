package com.PuzzleU.Server.university.repository;

import com.PuzzleU.Server.common.enumSet.UniversityType;
import com.PuzzleU.Server.university.entity.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    Page<University> findByUniversityNameContainingAndUniversityType(String searchKeyword, UniversityType universityType, Pageable pageable);
}
