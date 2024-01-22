package com.PuzzleU.Server.oauth;

import com.PuzzleU.Server.entity.kakao.AuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

// 7번 제작
/**
 * 사용자 인증 토큰을 생성하고 관리.
 */
@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {

    // 토큰의 타입 및 만료 시간 상수 설정
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    // JWT 토큰을 생성 및 관리하는 JwtTokenProvider
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 ID를 기반으로 Access Token과 Refresh Token을 생성하고,
     * 해당 정보로 AuthTokens 객체를 생성하여 반환.
     *
     * @param memberId 사용자 ID
     * @return 생성된 AuthTokens 객체
     */
    public AuthTokens generate(Long memberId) {
        // 현재 시간을 기준으로 만료 시간 설정
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        // 사용자 ID를 문자열로 변환하여 JWT 토큰 생성
        String subject = memberId.toString();
        String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);

        // AuthTokens 객체 생성 및 반환
        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    /**
     * Access Token에서 추출한 사용자 ID를 반환.
     *
     * @param accessToken 추출할 Access Token
     * @return 추출된 사용자 ID
     */
    public Long extractMemberId(String accessToken) {
        // JWT 토큰에서 subject를 추출하고, 해당 값을 Long으로 변환하여 반환
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }
}