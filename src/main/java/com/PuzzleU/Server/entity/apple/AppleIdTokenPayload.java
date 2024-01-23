package com.PuzzleU.Server.entity.apple;

import lombok.Getter;

@Getter
public class AppleIdTokenPayload {
    private String sub; // id토큰의 고유 식별자
    private String email;
}
