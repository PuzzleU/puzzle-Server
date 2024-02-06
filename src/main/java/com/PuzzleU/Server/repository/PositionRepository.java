package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.position.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByPositionId(Long positionId);
}
