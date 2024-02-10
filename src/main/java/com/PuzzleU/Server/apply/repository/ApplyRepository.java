package com.PuzzleU.Server.apply.repository;

import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.team.entity.Team;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findByTeam(Team team, Pageable pageable);
}
