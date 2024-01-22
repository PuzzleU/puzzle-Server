package com.PuzzleU.Server.service;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;
import com.PuzzleU.Server.service.SInterface.OAuthApiClient;
import com.PuzzleU.Server.service.SInterface.OAuthInfoResponse;
import com.PuzzleU.Server.service.SInterface.OAuthLoginParams;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
// 제작 5번
/**
 * OAuthLoginParams를 기반으로 OAuth 정보를 요청하는 서비스.
 * 서로 다른 제공자에 대한 OAuth 작업을 처리하기 위해 일련의 OAuthApiClient 구현.
 */
@Component
public class RequestOAuthInfoService {

    // OAuthApiClient 인스턴스를 OAuthProvider에 기반하여 저장하는 Map
    // provider 정보와, access token을 만들 수 있는 객체를 포함하는 client 생성
    private final Map<OAuthProvider, OAuthApiClient> clients;

    /**
     * RequestOAuthInfoService의 생성자.
     * clients 맵을 초기화하고 주어진 목록에서 OAuthApiClient 인스턴스를 수집.
     *
     * @param clients Spring에 의해 주입된 OAuthApiClient 인스턴스 목록.
     */
    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        // 클라이언트 목록을 OAuthProvider를 키로 사용하여 수정할 수 없는 맵으로 변환합니다.
        this.clients = clients.stream().collect(
                // access token 을 담당하는 OAuthApiClient를 리스트로 만든것을 연산을 위한 스트림으로 만든후에
                // 요소를 쉬집하기 위해 collect를 사용하여서 수행불가능한 맵으로변호나 - 키는 OauthApiClient 객체의 oAuthProvider며, 값은 클라이언트 그대로다)
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())


        );
        System.out.println(clients);
    }

    /**
     * OAuthLoginParams를 기반으로 OAuth 정보를 요청하는 메서드.
     * params에 포함된 OAuthProvider를 기반으로 적절한 OAuthApiClient를 가져와
     * 액세스 토큰을 요청하고, 액세스 토큰을 사용하여 OAuth 사용자 정보를 요청.
     *
     * @param params OAuth 요청에 필요한 정보를 담은 OAuthLoginParams.
     * @return OAuthProvider로부터 받은 사용자 정보를 포함하는 OAuthInfoResponse.
     */
    public OAuthInfoResponse request(OAuthLoginParams params) {
        // params의 OAuthProvider를 기반으로 적절한 OAuthApiClient를 가져옴.
        OAuthApiClient client = clients.get(params.oAuthProvider());

        // 클라이언트를 사용하여 OAuth 제공자에서 액세스 토큰을 요청.
        String accessToken = client.requestAccessToken(params);

        // 얻은 액세스 토큰을 사용하여 OAuth 사용자 정보를 요청.
        return client.requestOauthInfo(accessToken);
    }
}
