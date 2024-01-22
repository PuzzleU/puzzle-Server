package com.PuzzleU.Server.oauth;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;

public interface OAuthApiClient {
    // client 타입 반환
    OAuthProvider oAuthProvider();
    // Authorization code를 기반으로 인증 api 를 용청해서 access token 획득
    String requestAccessToken(OAuthLoginParams params);
    // 정보 획득
    OAuthInfoResponse requestOauthInfo(String accessToken);
}
