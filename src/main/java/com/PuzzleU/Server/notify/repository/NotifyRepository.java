package com.PuzzleU.Server.notify.repository;

import com.PuzzleU.Server.notify.entity.NotifyFriendShip;
import com.PuzzleU.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<NotifyFriendShip, Long> {

    List<NotifyFriendShip> findByUser(User user);

}
