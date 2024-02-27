package com.PuzzleU.Server.team.controller;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.competition.dto.CompetitionSearchTotalDto;
import com.PuzzleU.Server.friendship.dto.FriendShipSearchResponseDto;
import com.PuzzleU.Server.team.dto.AcceptOrRejectRequestDto;
import com.PuzzleU.Server.team.dto.TeamApplyListDto;
import com.PuzzleU.Server.team.dto.TeamCreateDto;
import com.PuzzleU.Server.team.service.TeamService;
import com.PuzzleU.Server.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {
    private final UserService userService;
    private final TeamService teamService;





    @GetMapping("/{teamId}/apply")
    public ApiResponseDto<TeamApplyListDto> readTeamApplyList(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long teamId) {
        return teamService.readTeamApplyList(loginUser, teamId);
    }

    @PatchMapping("{teamId}/apply/{applyId}")
    public ApiResponseDto<SuccessResponse> applyAcceptOrReject(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long teamId,
            @PathVariable Long applyId,
            @RequestBody AcceptOrRejectRequestDto acceptOrRejectDto) {
        return teamService.applyAcceptOrReject(loginUser, teamId, applyId, acceptOrRejectDto);
    }

}
