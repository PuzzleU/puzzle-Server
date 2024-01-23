package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.service.Impl.AppleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/oauth")
public class AppleController {
    final private AppleService appleService;

    @GetMapping("/apple")
    public ApiResponseDto<SuccessResponse> appleLogin(@RequestParam("code") String code)
    {
        return appleService.appleLogin(code);
    }

}
