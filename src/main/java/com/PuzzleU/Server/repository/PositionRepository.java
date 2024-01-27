package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.position.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
