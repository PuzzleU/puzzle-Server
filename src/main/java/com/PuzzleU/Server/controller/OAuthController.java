package com.PuzzleU.Server.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {
    /**
     * 카카오 callback
     * [GET] /oauth/kakao
     * 이 과정은 클라이언트에서 처리! 테스트용으로 만들어 두었음.
     */

    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam("code") String code) {
        System.out.println(code);
    }
}
