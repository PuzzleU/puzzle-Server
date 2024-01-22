package com.PuzzleU.Server.entity.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokens {

    private String accessToken; // 엑세스 토큰을 반환
    private String refreshToken; // refreshToken을 반환
    private String grantType; // 타입 반환
    private Long expiresIn; // 만료일 반환

    // AuthToken을 구성체를 통해서 생성해서 AuthToken을 생성한다
    public static AuthTokens of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        return new AuthTokens(accessToken, refreshToken, grantType, expiresIn);
    }
}
