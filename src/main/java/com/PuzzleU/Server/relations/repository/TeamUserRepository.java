package com.PuzzleU.Server.relations.repository;

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

}
