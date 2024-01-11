package com.PuzzleU.Server.entity;

import com.PuzzleU.Server.entity.enumSet.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 나중에 추가가능
@Getter
@Entity(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length =30)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    @Builder
    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public static User of(String username, String password, UserRoleEnum role)
    {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }
}
