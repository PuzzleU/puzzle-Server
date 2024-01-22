package com.PuzzleU.Server.service.SInterface;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;

// 카카오의 응답값을 리턴해주는 인터페이스

public interface OAuthApiClient {
    // client 타입 반환
    OAuthProvider oAuthProvider();
    // Authorization code를 기반으로 인증 api 를 용청해서 access token 획득
    String requestAccessToken(OAuthLoginParams params);
    // 정보 획득
    OAuthInfoResponse requestOauthInfo(String accessToken);
}
