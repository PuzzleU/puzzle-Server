package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.heart.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Heart, Long> {
}
