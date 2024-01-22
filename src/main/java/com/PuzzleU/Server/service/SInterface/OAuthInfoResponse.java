package com.PuzzleU.Server.service.SInterface;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;


// access 토큰으로 요청한 외부 API 프로필 응답값을 우리 서비스의 Model로 변환시키기 위한 것
public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
