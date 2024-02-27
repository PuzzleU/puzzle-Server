package com.PuzzleU.Server.heart.repository;

import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.heart.entity.Heart;
import com.PuzzleU.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long>
{
    @Query("SELECT h FROM Heart h WHERE h.competition=:competition and h.user!=:user")
    List<Heart> findByCompetitionAndUser(Competition competition, User user);

    @Query("SELECT h FROM Heart h WHERE h.competition=:competition and h.user=:user")
    List<Heart> findByCompetitionAndUserNot(Competition competition, User user);

    @Query("SELECT h FROM Heart h WHERE h.competition=:competition and h.user=:user")
    Heart findOneByCompetitionAndUserNot(Competition competition, User user);

    @Query("SELECT c FROM Heart c WHERE c.competition.competitionId=:competitionId")
    List<Heart> findByCompetition(Long competitionId);
}
