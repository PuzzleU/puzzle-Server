package com.PuzzleU.Server.oauth;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;
import com.PuzzleU.Server.service.SInterface.OAuthInfoResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

// 3번 제작
/**
 * KakaoInfoResponse 클래스는 Kakao OAuth 서비스로부터 받은 사용자 정보를 담는 클래스
 * OAuthInfoResponse 인터페이스를 구현하고, 사용자의 이메일, 닉네임, OAuthProvider를 제공.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    // Kakao 서비스로부터 받은 사용자 정보 중 KakaoAccount 객체를 매핑
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    /**
     * KakaoAccount 클래스는 Kakao 서비스로부터 받은 사용자 계정 정보를 담는 내부 클래스.
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        // KakaoAccount 객체 내부에 KakaoProfile 객체와 이메일이 매핑
        private KakaoProfile profile;
        private String email;
    }

    /**
     * KakaoProfile 클래스는 Kakao 서비스로부터 받은 사용자 프로필 정보를 담는 내부 클래스.
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        // KakaoProfile 객체 내부에 닉네임이 매핑
        private String nickname;
    }

    // OAuthInfoResponse 인터페이스의 메서드를 구현
    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}