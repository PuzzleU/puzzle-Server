package com.PuzzleU.Server.notification.repository;

import com.PuzzleU.Server.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyFriendShipRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserId(Long id);
}
