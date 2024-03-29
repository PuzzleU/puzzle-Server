package com.PuzzleU.Server.team.repository;

import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.team.entity.Team;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t join fetch t.competition WHERE t.competition =:competition")
    List<Team> findByCompetition(@Param("competition") Competition competition, Pageable pageable);

    List<Team> findByTeamTitleContaining(String search, Pageable pageable);

    Optional<Team> findByTeamId(Long teamId);

    @Query("SELECT t FROM Team t join fetch t.competition WHERE t.competition.competitionId =:CompetitionId and t.teamId =:teamId")
    Optional<Team> findByTeamIdAndCompetitionId(Long teamId, Long CompetitionId);



}
