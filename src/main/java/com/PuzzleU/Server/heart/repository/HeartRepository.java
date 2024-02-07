package com.PuzzleU.Server.heart.repository;

import com.PuzzleU.Server.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long>
{
}
