package com.PuzzleU.Server.common.enumSet;

import lombok.Getter;

@Getter
public enum UserRoleEnum {

    // Authority를 통해 특정 문자열을 enum과 연결한다
    // 특정 url 접속시 권한과 역할을 연결한다
    USER(Authority.USER),
    ADMIN(Authority.ADMIN);



    private final String authority;

    UserRoleEnum(String authority)
    {
        this.authority = authority;
    }

    public static class Authority{
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
