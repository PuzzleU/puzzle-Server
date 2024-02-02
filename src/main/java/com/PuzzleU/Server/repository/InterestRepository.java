package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.enumSet.InterestTypes;
import com.PuzzleU.Server.entity.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByInterestType(InterestTypes interestType);
}
