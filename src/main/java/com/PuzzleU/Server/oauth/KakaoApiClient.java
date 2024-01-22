package com.PuzzleU.Server.oauth;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;
import com.PuzzleU.Server.entity.kakao.KakaoTokens;
import com.PuzzleU.Server.service.SInterface.OAuthApiClient;
import com.PuzzleU.Server.service.SInterface.OAuthInfoResponse;
import com.PuzzleU.Server.service.SInterface.OAuthLoginParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


// 4번 제작
/**
 * KakaoApiClient 클래스는 Kakao OAuth 서비스와 통신하여 Access Token을 요청,
 * 해당 Access Token을 사용하여 사용자 정보를 조회하는 역할을 담당하는 컴포넌트.
 */
@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    // OAuth Kakao 서비스 관련 URL 및 클라이언트 ID
    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    // 내 restapi
    @Value("${oauth.kakao.client-id}")
    private String clientId;

    // Spring의 RestTemplate을 사용하여 HTTP 통신
    private final RestTemplate restTemplate;

    // OAuthProvider.KAKAO를 반환하여 Kakao 서비스임을 식별
    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    /**
     * 주어진 OAuthLoginParams를 사용하여 Kakao OAuth 서비스에 Access Token을 요청합니다.
     *
     * @param params OAuthLoginParams 객체 (인증 코드 등)
     * @return 요청한 Access Token
     */
    @Override
    // OAuthLoginParams에는 KAKAO를 쓸건지를 설정하는 부분과 BODY를 만들수 있는 것
    // AUTHORIZATION코드를 넣은 MAP이 저장되어있다
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/oauth/token";

        // 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // OAuthLoginParams를 사용하여 MultiValueMap 생성
        // 코드등 다양한 정보를 BODY에 넣는다
        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);

        // HttpEntity형인 request에 정보들을 저장해 놓은 body와 header설정된 것들을 만들어준다
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        // KakaoTokens 클래스로 응답을 받아 Access Token 추출
        // response는 restTemplate 형식이며, 각각 url, request, kakaotokens의 class형식이 저장이 된다
        KakaoTokens response = restTemplate.postForObject(url, request, KakaoTokens.class);

        assert response != null;
        // access토큰을 반환해준다
        return response.getAccessToken();
    }

    /**
     * 주어진 Access Token을 사용하여 Kakao OAuth 서비스에 사용자 정보를 요청.
     * 위의 access 토큰을 기반으로 한다
     * @param accessToken Kakao OAuth 서비스로부터 발급받은 Access Token
     * @return 요청한 사용자 정보
     */
    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        // 사용자 정보를 요청하는 MultiValueMap 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        // KakaoInfoResponse 클래스로 응답을 받아 OAuthInfoResponse로 변환하여 반환
        return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
    }
}