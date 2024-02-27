package com.PuzzleU.Server.notify.repository;

import com.PuzzleU.Server.notify.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
}
