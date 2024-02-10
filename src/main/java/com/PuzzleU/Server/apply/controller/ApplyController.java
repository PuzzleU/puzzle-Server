package com.PuzzleU.Server.apply.controller;

import com.PuzzleU.Server.apply.dto.ApplyPostDto;
import com.PuzzleU.Server.apply.service.ApplyService;
import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/apply")
public class ApplyController {
    private final ApplyService applyService;

    @PostMapping("/team/{teamId}")
    public ApiResponseDto<SuccessResponse> postApply(
    @AuthenticationPrincipal UserDetails loginUser,
    @PathVariable Long teamId,
    @RequestBody ApplyPostDto applyPostDto) {
        return applyService.postApply(loginUser, teamId, applyPostDto);
    }
}
