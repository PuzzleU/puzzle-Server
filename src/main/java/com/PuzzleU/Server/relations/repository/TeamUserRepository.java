package com.PuzzleU.Server.relations.repository;

import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.relations.entity.TeamUserRelation;
import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.user.entity.User;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamUserRepository extends JpaRepository<TeamUserRelation, Long> {
    @Query("SELECT f FROM TeamUserRelation  f WHERE (f.user = :user AND f.team =:team)")
    Optional<TeamUserRelation> findByUserAndTeam(@Param("user")User user, @Param("team") Team team);


    List<TeamUserRelation> findByTeam(Team team);

    @Query("SELECT f.team FROM TeamUserRelation f WHERE (f.user = :user AND f.isWriter=true)")
    List<Team> findByUserAndWaitingOne(@Param("user") User user);

    @Query("SELECT f.team FROM TeamUserRelation f WHERE (f.user = :user AND f.isWriter=false)")
    List<Team> findByUserAndEndOne(@Param("user") User user);

    @Query("SELECT tur FROM TeamUserRelation tur JOIN Team t ON tur.team = t JOIN Competition c ON t.competition = c WHERE tur.user = :user AND c = :competition")
    Optional<TeamUserRelation> findByUserAndCompetitionExist(User user, Competition competition);

    @Query("SELECT tu.user FROM TeamUserRelation tu WHERE tu.team=:team and tu.isWriter =true")
    Optional<User>findWriterByTeam(@Param("team")Team team);
}
