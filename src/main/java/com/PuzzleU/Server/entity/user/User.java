package com.PuzzleU.Server.entity.user;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;
import com.PuzzleU.Server.entity.enumSet.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 나중에 추가가능
@Getter
@Entity(name="user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String email;

    private String username;

    private String password;

    @Column(nullable = false)
    private  String nickname;

    private OAuthProvider oAuthProvider;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User(String username, String password, String email, UserRoleEnum role, String nickname, OAuthProvider oAuthProvider) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.oAuthProvider = oAuthProvider;
    }
    public static User of(String username, String password, UserRoleEnum role, String email, String nickname, OAuthProvider oAuthProvider)
    {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .email(email)
                .nickname(nickname)
                .oAuthProvider(oAuthProvider)
                .build();
    }
}
