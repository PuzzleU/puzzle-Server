package com.PuzzleU.Server.common.oauth;

import com.PuzzleU.Server.apple.AuthService;
import com.PuzzleU.Server.apple.SignInRequest;
import com.PuzzleU.Server.apple.SignInResponse;
import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.enumSet.LoginType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.common.jwt.TokenDto;
import com.PuzzleU.Server.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// 테스트용
import com.PuzzleU.Server.user.entity.User;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {
    final private OAuthService oAuthService;
    // 테스트용
    final private UserRepository userRepository;

    /**
     * 카카오 callback
     * [GET] /oauth/kakao
     * 이 과정은 클라이언트에서 처리! 테스트용으로 만들어 두었음.
     */

    @PostMapping("/kakao")
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
    private final AuthService authService;

    @PostMapping("apple")
    public ApiResponseDto<SignInResponse> signIn(@RequestHeader("Authorization") String socialAccessToken, @RequestBody SignInRequest request) {
        SignInResponse response = authService.signIn(socialAccessToken, request);
        return ResponseUtils.ok(response, null);
    }

    @PostMapping("apple/logout")
    public ApiResponseDto<SuccessResponse> signOut(Principal principal) {
        long memberId = Long.parseLong(principal.getName());
        authService.signOut(memberId);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK,"로그아웃성공"),null);
    }

    @DeleteMapping("apple/withdrawl")
    public ApiResponseDto<SuccessResponse> withdrawal(Principal principal) {
        long memberId = Long.parseLong(principal.getName());
        authService.withdraw(memberId);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK,"withdrawl성공"),null);
    }

}
