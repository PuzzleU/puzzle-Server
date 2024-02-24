package com.PuzzleU.Server.competition.controller;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.competition.dto.CompetitionHomeTotalDto;
import com.PuzzleU.Server.competition.dto.CompetitionSearchTotalDto;
import com.PuzzleU.Server.competition.dto.CompetitionSpecificDto;
import com.PuzzleU.Server.common.enumSet.CompetitionType;
import com.PuzzleU.Server.competition.service.CompetitionService;
import com.PuzzleU.Server.heart.Service.HeartService;
import com.PuzzleU.Server.team.dto.TeamListDto;
import com.PuzzleU.Server.team.dto.TeamSpecificDto;
import com.PuzzleU.Server.team.service.TeamService;
import com.PuzzleU.Server.user.dto.UserSimpleDto;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/competition")
public class CompetitionController {

    private final CompetitionService competitionService;
    private final TeamService teamService;
    private final UserService userService;
    private final HeartService heartService;

    @GetMapping("/homepage")
    public ApiResponseDto<CompetitionHomeTotalDto> homepage(
            @RequestParam(value = "competitionType", required = false) CompetitionType competitionType,
            @RequestParam(value = "search", defaultValue = "None", required = false) String search,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "CompetitionId", required = false) String sortBy
    ) {
        return competitionService.getHomepage(pageNo, pageSize, sortBy, search, competitionType);
    }
    @GetMapping("/homepage/{competition_id}")
    public ApiResponseDto<CompetitionSpecificDto> specific(@Valid
            @PathVariable Long competition_id)
    {
        return competitionService.getSpecific(competition_id);
    }
    @GetMapping("/homepage/{competition_id}/team")
    public ApiResponseDto<TeamListDto> teamList(@Valid
    @PathVariable Long competition_id,
    @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
    @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
    @RequestParam(value = "sortBy", defaultValue = "teamId", required = false) String sortBy)
    {
        return teamService.getTeamList(competition_id, pageNo, pageSize, sortBy);
    }
    @GetMapping("/homepage/{competition_id}/team/{team_id}")
    public ApiResponseDto<TeamSpecificDto> teamSpecific(@Valid @PathVariable Long competition_id, @PathVariable Long team_id)
    {
        return teamService.getTeamSpecific(competition_id, team_id);
    }
    @GetMapping("/homepage/team")
    public ApiResponseDto<TeamListDto> teamSearch(@Valid
                                                  @RequestParam(value = "search", defaultValue = "None", required = false) String search,
                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
                                                @RequestParam(value = "sortBy", defaultValue = "teamId", required = false) String sortBy)
    {
        return teamService.getTeamSearchList(pageNo, pageSize, sortBy,search);
    }
    @GetMapping("/homepage/competition")
    public ApiResponseDto<CompetitionSearchTotalDto> competitionSearch(@Valid
                                                  @RequestParam(value = "search", defaultValue = "None", required = false) String search,
                                                                       @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                       @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "competitionId", required = false) String sortBy)
    {
        return teamService.competitionTeamSearch(pageNo,pageSize, sortBy, search);
    }
    @GetMapping("/homepage/users")
    public ApiResponseDto<List<UserSimpleDto>> userSearch(@Valid
                                                              @AuthenticationPrincipal UserDetails loginUser,
                                                           @RequestParam(value = "search", defaultValue = "None", required = false) String search,
                                                          @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                          @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = "competitionId", required = false) String sortBy)
    {
        return userService.userSearch(loginUser, pageNo, pageSize, sortBy, search);
    }
    @PatchMapping("/homepage/{competition_id}")
    public ApiResponseDto<SuccessResponse> heartCreate(@Valid
    @AuthenticationPrincipal UserDetails loginUser,
    @PathVariable Long competition_id)
    {
        return heartService.heartCreate(loginUser, competition_id);
    }
    @DeleteMapping("/homepage/{competition_id}")
    public ApiResponseDto<SuccessResponse> heartDelete(@Valid

                                                       @AuthenticationPrincipal UserDetails loginUser,
                                                       @PathVariable Long competition_id)
    {
        return heartService.heartDelete(loginUser, competition_id);
    }



}