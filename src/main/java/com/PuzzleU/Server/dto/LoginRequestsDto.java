package com.PuzzleU.Server.dto;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;
import lombok.Getter;


@Getter
public class LoginRequestsDto {

    private String username;
    private String password;
    private String email;
    private String nickname;
    private OAuthProvider oAuthProvider;

}