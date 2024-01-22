package com.PuzzleU.Server.oauth;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;
import com.PuzzleU.Server.service.SInterface.OAuthLoginParams;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
// 1번  제작
/**
 * KakaoLoginParams 클래스는 Kakao OAuth 서비스로부터 받은 인가 코드(authorization code)를 담는 클래스.
 * 해당 클래스는 OAuthLoginParams 인터페이스를 구현하고, OAuthProvider 및 인가 코드를 제공.
 */
@Getter
@NoArgsConstructor
public class KakaoLoginParams implements OAuthLoginParams {

    // Kakao OAuth 서비스로부터 받은 인가 코드
    private String authorizationCode;

    // OAuthProvider 인터페이스의 메서드를 구현
    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    /**
     * OAuthLoginParams 인터페이스의 메서드를 구현하여 인가 코드를 MultiValueMap 형태로 반환.
     * @return MultiValueMap<String, String> - 인가 코드를 담은 MultiValueMap
     */
    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        // code에 authorizationCode를 넣어서 반환해준다
        body.add("code", authorizationCode);
        return body;
    }
}