package com.PuzzleU.Server.common.oauth;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.jwt.TokenDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {
    final private OAuthService oAuthService;

    /**
     * 카카오 callback
     * [GET] /oauth/kakao
     * 이 과정은 클라이언트에서 처리! 테스트용으로 만들어 두었음.
     */

    @GetMapping("/kakao")
    public ApiResponseDto<TokenDto> kakaoLogin(@RequestParam("kakaoAccessToken") String kakaoAccessToken) {
//        System.out.println("code = " + code); // 카카오 로그인을 수행하면 /api/oauth/kakao로 리다이렉트 되면서 code가 받아와짐
//
//        String accessToken = oAuthService.getKakaoAccessToken(code); // access token 받아오기
//        System.out.println("accessToken = " + accessToken);
//
//        KakaoUserInfoDto kakaoUserInfoDto = oAuthService.getKakaoUserInfo(accessToken);
//        System.out.println("kakaoUserInfo = " + kakaoUserInfoDto.toString());

        return oAuthService.kakaoLogin(kakaoAccessToken);
    }

    @PostMapping("/kakao/refresh")
    public ApiResponseDto<TokenDto> refreshToken(
            @RequestHeader(value="accessToken") String accessToken,
            @RequestHeader(value="refreshToken") String refreshToken
    ) {
        return oAuthService.refreshKakaoToken(accessToken, refreshToken);
    }

}
