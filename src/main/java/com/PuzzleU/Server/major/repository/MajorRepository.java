package com.PuzzleU.Server.major.repository;

import com.PuzzleU.Server.major.entity.Major;
import com.PuzzleU.Server.university.entity.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {

    List<Major> findByUniversityAndMajorNameContaining(University university, String majorName);


}
