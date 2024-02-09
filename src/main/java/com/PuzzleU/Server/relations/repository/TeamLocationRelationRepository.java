package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.relations.entity.TeamLocationRelation;
import com.PuzzleU.Server.team.entity.Team;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamLocationRelationRepository extends JpaRepository<TeamLocationRelation, Long> {

    @Query("SELECT t FROM TeamLocationRelation t WHERE t.team =:team")
    List<TeamLocationRelation> findByTeam(@Param("team") Team team);
}
