package com.PuzzleU.Server.team.repository;

import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.team.entity.Team;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t WHERE t.competition =:competition")
    List<Team> findByCompetition(@Param("competition") Competition competition, Pageable pageable);
}
