package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.LoginRequestsDto;
import com.PuzzleU.Server.dto.SignupRequestDto;
import com.PuzzleU.Server.entity.kakao.AuthTokens;
import com.PuzzleU.Server.oauth.KakaoLoginParams;
import com.PuzzleU.Server.service.OAuthLoginService;
import com.PuzzleU.Server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "유저 정보 api")

public class UserController {

    private final OAuthLoginService oAuthLoginService;
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponseDto<SuccessResponse> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }
    // 기존에는 SuccessResponseDto를 통해서 userrequestsdto를 통하여 주입을 해주었다
    /*
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto> login(@RequestBody UserReqestsDto reqestsDto, BindingResult bindingResult)
    {
        return userService.signup(requestDto, bindingResult);
    }
    */
    @Parameter(name = "userId", description = "유저 아이디", required = true)
    @Operation(summary = "Login", description = "유저 아이디로 로그인 실행")
    @PostMapping("/login")
    public ApiResponseDto<SuccessResponse> login(@RequestBody LoginRequestsDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }

    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params)
    {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }


/*
    @PostMapping("/signout")
    public ApiResponseDto<SuccessResponse> signout(@RequestBody LoginRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.signout(requestDto, userDetails.getUser());
    }

 */
}

