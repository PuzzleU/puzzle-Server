package com.PuzzleU.Server.user.repository;

import com.PuzzleU.Server.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByUserKoreaNameContaining(String username, Pageable pageable);

}
