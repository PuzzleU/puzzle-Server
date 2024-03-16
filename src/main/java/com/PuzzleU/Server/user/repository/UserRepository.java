package com.PuzzleU.Server.user.repository;

import com.PuzzleU.Server.common.enumSet.LoginType;
import com.PuzzleU.Server.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByUserKoreaNameContaining(String username, Pageable pageable);

    @Query("SELECT u FROM users u WHERE u!=:user")
    List<User> findAllExcept(User user, Pageable pageable);

    Optional<User> findByUserPuzzleId(String puzzleId);

    Optional<User> findByUsernameAndLoginType(String username, LoginType loginType);
    Optional<User> findByLoginTypeAndUsername(LoginType socialType, String socialId);

    Optional<User> findById(Long Id);
}
