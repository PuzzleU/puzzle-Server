package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.LoginRequestsDto;
import com.PuzzleU.Server.dto.SignupRequestDto;
import com.PuzzleU.Server.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
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

    @PostMapping("/login")
    public ApiResponseDto<SuccessResponse> login(@RequestBody LoginRequestsDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }
/*
    @PostMapping("/signout")
    public ApiResponseDto<SuccessResponse> signout(@RequestBody LoginRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.signout(requestDto, userDetails.getUser());
    }

 */
}

