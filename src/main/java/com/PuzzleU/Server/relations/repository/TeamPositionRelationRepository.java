package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.relations.entity.TeamPositionRelation;
import com.PuzzleU.Server.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamPositionRelationRepository extends JpaRepository<TeamPositionRelation, Long> {

    @Query("SELECT t.position FROM TeamPositionRelation t WHERE t.team =:team")

    List<Position>findByTeam(Team team);

}
