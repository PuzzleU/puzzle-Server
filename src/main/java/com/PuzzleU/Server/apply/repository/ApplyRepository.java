package com.PuzzleU.Server.apply.repository;

import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.user.entity.User;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {

    @Query("SELECT a.team FROM Apply a WHERE a.user = :user")
    List<Team> findByUser(User user, org.springframework.data.domain.Pageable pageable);
    @Query("SELECT a.team FROM Apply a WHERE a.user = :user AND a.applyStatus = 'WAITING'")
    List<Team> findByUserAndApplyStatusIsWaiting(User user, org.springframework.data.domain.Pageable pageable);
    @Query("SELECT a.team FROM Apply a WHERE a.user = :user AND (a.applyStatus = 'ACCEPTED' OR a.applyStatus = 'REJECTED')")
    List<Team> findByUserAndApplyStatusIsFinished(User user, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT DISTINCT a.team FROM Apply a WHERE a.user = :user AND a.applyStatus = 'WAITING'")
    List<Team> findFirstByUserAndApplyStatusIsWaitingOne(User user);

    @Query("SELECT DISTINCT a.team FROM Apply a WHERE a.user = :user AND (a.applyStatus = 'ACCEPTED' OR a.applyStatus = 'REJECTED')")
    List<Team> findFirstByUserAndApplyStatusIsFinishedOne(User user);

    Optional<Apply> findByUserAndTeam(User user, Team team);
    Optional<Apply> findByApplyId(Long applyId);
}
