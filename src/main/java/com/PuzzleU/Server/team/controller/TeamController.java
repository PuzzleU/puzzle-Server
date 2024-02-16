package com.PuzzleU.Server.team.controller;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.competition.dto.CompetitionSearchTotalDto;
import com.PuzzleU.Server.friendship.dto.FriendShipSearchResponseDto;
import com.PuzzleU.Server.team.dto.TeamApplyListDto;
import com.PuzzleU.Server.team.dto.TeamCreateDto;
import com.PuzzleU.Server.team.service.TeamService;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
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

    @PostMapping("")
    public ApiResponseDto<SuccessResponse> teamCreate(@Valid @RequestBody TeamCreateDto teamCreateDto
    ,@RequestParam Long competitionId,
     @RequestParam List<Long> teamMember,
     @AuthenticationPrincipal UserDetails loginUser,
    @RequestParam List<Long> positions,
    @RequestParam List<Long> locations) {
        return teamService.teamcreate(teamCreateDto, competitionId, teamMember,loginUser,locations, positions);
    }

    @PatchMapping("/{teamId}")
    public ApiResponseDto<SuccessResponse> teamUpdate(@Valid @RequestBody TeamCreateDto teamCreateDto
            ,@RequestParam Long competitionId,
            @RequestParam List<Long> teamMember,
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestParam List<Long> locations,
            @RequestParam List<Long> positions,
            @PathVariable Long teamId) {
        return teamService.teamUpdate(teamCreateDto, competitionId, teamMember,loginUser,locations, positions, teamId);
    }
    @DeleteMapping("/{teamId}")
    public ApiResponseDto<SuccessResponse> teamDelete(@Valid
            @PathVariable Long teamId,
            @AuthenticationPrincipal UserDetails loginUser
            ) {
        return teamService.teamdelete(teamId,loginUser);
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
            @RequestParam(value = "sortBy", defaultValue = "friend_ship_id", required = false) String sortBy,
            @AuthenticationPrincipal UserDetails loginUser)
    {
        return teamService.getfriendRegister(keyword, loginUser,pageNo,pageSize, sortBy);
    }

    @GetMapping("/{teamId}/apply")
    public ApiResponseDto<TeamApplyListDto> readTeamApplyList(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long teamId) {
        return teamService.readTeamApplyList(loginUser, teamId);
    }


}
