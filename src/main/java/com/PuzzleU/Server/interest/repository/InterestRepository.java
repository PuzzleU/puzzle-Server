package com.PuzzleU.Server.interest.repository;

import com.PuzzleU.Server.common.enumSet.InterestTypes;
import com.PuzzleU.Server.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByInterestType(InterestTypes interestType);
    Optional<Interest> findByInterestId(Long interestId);
}
