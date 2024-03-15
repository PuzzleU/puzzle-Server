package com.PuzzleU.Server.notify.repository;

import com.PuzzleU.Server.notify.entity.Notification;
import com.PuzzleU.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);

    @Query("select n from Notification n Where n.user =:user and n.isRead = false")
    List<Notification> findByUserAndUnread(User user);

}
