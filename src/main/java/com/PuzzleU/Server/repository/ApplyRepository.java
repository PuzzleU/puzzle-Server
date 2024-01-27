package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.apply.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
}
