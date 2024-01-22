package com.PuzzleU.Server.service;

import com.PuzzleU.Server.entity.enumSet.UserRoleEnum;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.entity.kakao.AuthTokens;
import com.PuzzleU.Server.oauth.AuthTokensGenerator;
import com.PuzzleU.Server.repository.UserRepository;
import com.PuzzleU.Server.service.SInterface.OAuthInfoResponse;
import com.PuzzleU.Server.service.SInterface.OAuthLoginParams;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *  외부 OAuth 제공자에서 사용자 정보를 가져와
 * 로그인 및 회원가입과 관련된 작업을 처리하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final EntityManager entityManager;
    // 사용자 저장소
    private final UserRepository userRepository;

    // OAuth 토큰 생성기
    private final AuthTokensGenerator authTokensGenerator;

    // 외부 OAuth 제공자로부터 정보를 요청하는 서비스
    private final RequestOAuthInfoService requestOAuthInfoService;

    /**
     * OAuth 제공자에서 받은 정보를 기반으로 사용자를 로그인 또는 회원가입하고
     * 사용자에게 새로운 AuthTokens를 생성하여 반환합니다.
     *
     * @param params OAuth 제공자에서 받은 정보를 포함하는 OAuthLoginParams
     * @return 새로운 AuthTokens
     */
    public AuthTokens login(OAuthLoginParams params) {
        // OAuth 제공자로부터 정보를 요청하고 받아옵니다.
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);

        // 사용자를 찾거나 생성하고, 해당 사용자의 ID를 얻어옵니다.
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        System.out.println(memberId);
        // 얻어온 사용자 ID를 사용하여 AuthTokens를 생성합니다.
        return authTokensGenerator.generate(memberId);
    }

    /**
     * OAuth 제공자에서 받은 정보를 기반으로 사용자를 찾거나 생성하고,
     * 해당 사용자의 ID를 반환합니다.
     *
     * @param oAuthInfoResponse OAuth 제공자에서 받은 사용자 정보
     * @return 사용자의 ID
     */
    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        System.out.println(userRepository.findByEmail(oAuthInfoResponse.getEmail()));
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));

    }

    /**
     * 새로운 사용자를 생성하고, 생성된 사용자의 ID를 반환.
     *
     * @param oAuthInfoResponse OAuth 제공자에서 받은 사용자 정보
     * @return 생성된 사용자의 ID
     */
    @Transactional
    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        // 새로운 사용자 객체를 생성합니다.
        User member = User.builder()
                .username("DEFAULT")
                .password("1234")
                .email(oAuthInfoResponse.getEmail())
                .role(UserRoleEnum.valueOf("USER"))
                .nickname(oAuthInfoResponse.getNickname())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();
        System.out.println(member.getPassword());
        System.out.println(member.getEmail());
        System.out.println(member);

        User savedMember = userRepository.save(member);
        System.out.println(savedMember); // 저장된 엔터티의 정보 출력
        System.out.println("Saved User Information:");
        System.out.println("ID: " + savedMember.getId());
        System.out.println("Username: " + savedMember.getUsername());
        System.out.println("Password: " + savedMember.getPassword());
        System.out.println("Email: " + savedMember.getEmail());
        System.out.println("Role: " + savedMember.getRole());
        System.out.println("Nickname: " + savedMember.getNickname());
        System.out.println("OAuthProvider: " + savedMember.getOAuthProvider());

        // 사용자 저장소에 저장하고, 저장된 사용자의 ID를 반환합니다.
        return member.getId();
    }
}
