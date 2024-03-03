package com.PuzzleU.Server.notify.repository;

import com.PuzzleU.Server.notify.entity.NotifyFriendShip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyFriendShipRepository extends JpaRepository<NotifyFriendShip, Long> {
    List<NotifyFriendShip> findAllByUserId(Long id);
}
