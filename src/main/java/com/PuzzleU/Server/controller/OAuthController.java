package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.service.Impl.OAuthService;
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
    public ApiResponseDto<SuccessResponse> kakaoLogin(@RequestParam("code") String code) {
//        System.out.println("code = " + code); // 카카오 로그인을 수행하면 /api/oauth/kakao로 리다이렉트 되면서 code가 받아와짐
//
//        String accessToken = oAuthService.getKakaoAccessToken(code); // access token 받아오기
//        System.out.println("accessToken = " + accessToken);
//
//        KakaoUserInfoDto kakaoUserInfoDto = oAuthService.getKakaoUserInfo(accessToken);
//        System.out.println("kakaoUserInfo = " + kakaoUserInfoDto.toString());

        return oAuthService.kakaoLogin(code);
    }
}
