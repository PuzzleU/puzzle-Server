package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByUserKoreaNameContaining(String username);

}
