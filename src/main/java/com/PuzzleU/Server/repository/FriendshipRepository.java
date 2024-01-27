package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.friendship.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendShip, Long> {
}
