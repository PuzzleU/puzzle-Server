package com.PuzzleU.Server.position.repository;

import com.PuzzleU.Server.position.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByPositionId(Long positionId);
}
