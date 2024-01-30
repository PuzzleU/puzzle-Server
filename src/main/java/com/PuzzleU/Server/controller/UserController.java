package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.user.LoginRequestsDto;
import com.PuzzleU.Server.dto.user.SignupRequestDto;
import com.PuzzleU.Server.dto.user.UserRegisterOptionalDto;
import com.PuzzleU.Server.service.User.UserRegisterOptionalService;
import com.PuzzleU.Server.service.User.UserService;
import feign.Param;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserRegisterOptionalService userRegisterOptionalService;

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

    @PatchMapping("/register/{userId}")
    public ApiResponseDto<SuccessResponse> registerOptional(
            @RequestBody UserRegisterOptionalDto userRegisterOptionalDto,
            @PathVariable Long userId)
    {
        return userRegisterOptionalService.createRegisterOptionalUser(userId,userRegisterOptionalDto);
    }
}

