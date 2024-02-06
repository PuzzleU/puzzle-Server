package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.competition.CompetitionSearchDto;
import com.PuzzleU.Server.dto.competition.CompetitionSearchTotalDto;
import com.PuzzleU.Server.dto.friendship.FriendShipSearchResponseDto;
import com.PuzzleU.Server.dto.team.TeamCreateDto;
import com.PuzzleU.Server.dto.user.SignupRequestDto;
import com.PuzzleU.Server.service.User.UserService;
import com.PuzzleU.Server.service.team.TeamService;
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

    @PostMapping("/")
    public ApiResponseDto<SuccessResponse> teamCreate(@Valid @RequestBody TeamCreateDto teamCreateDto) {
        return teamService.teamcreate(teamCreateDto);
    }
    @GetMapping("/searchCompetition")
    public ApiResponseDto<CompetitionSearchTotalDto> competitionSearch(@Valid
      @RequestParam(value = "search", defaultValue = "None", required = false) String keyword,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
      @RequestParam(value = "sortBy", defaultValue = "competitionId", required = false) String sortBy)
    {
        return teamService.competitionTeamSearch(pageNo,pageSize, sortBy, keyword);
    }
    @GetMapping("/searchMember")
    public ApiResponseDto<FriendShipSearchResponseDto> memberSearch(
            @Valid
            @RequestParam(value = "search", defaultValue = "None", required = false) String keyword,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "friendshipId", required = false) String sortBy,
            @AuthenticationPrincipal UserDetails loginUser)
    {
        return teamService.firendRegister(keyword, loginUser,pageNo,pageSize, sortBy);
    }

}
