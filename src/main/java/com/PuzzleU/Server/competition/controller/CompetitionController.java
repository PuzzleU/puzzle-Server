package com.PuzzleU.Server.competition.controller;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.competition.dto.CompetitionHomeTotalDto;
import com.PuzzleU.Server.competition.dto.CompetitionSearchTotalDto;
import com.PuzzleU.Server.competition.dto.CompetitionSpecificDto;
import com.PuzzleU.Server.common.enumSet.CompetitionType;
import com.PuzzleU.Server.competition.service.CompetitionService;
import com.PuzzleU.Server.friendship.dto.FriendShipSearchResponseDto;
import com.PuzzleU.Server.heart.Service.HeartService;
import com.PuzzleU.Server.team.dto.TeamCreateDto;
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

    // 홈페이지
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
    // 공모전 세부페이지
    @GetMapping("/homepage/{competition_id}")
    public ApiResponseDto<CompetitionSpecificDto> specific(@Valid
            @PathVariable Long competition_id)
    {
        return competitionService.getSpecific(competition_id);
    }
    // 특정 공모전 모집중인 팀들 보기
    @GetMapping("/homepage/{competition_id}/team")
    public ApiResponseDto<TeamListDto> teamList(@Valid
    @PathVariable Long competition_id,
    @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
    @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
    @RequestParam(value = "sortBy", defaultValue = "teamId", required = false) String sortBy)
    {
        return teamService.getTeamList(competition_id, pageNo, pageSize, sortBy);
    }
    // 특정 공모전의 모집중인 특정 공모전 보기
    @GetMapping("/homepage/{competition_id}/team/{team_id}")
    public ApiResponseDto<TeamSpecificDto> teamSpecific(@Valid @PathVariable Long competition_id, @PathVariable Long team_id)
    {
        return teamService.getTeamSpecific(competition_id, team_id);
    }
    // 공모전 팀 검색하기
    @GetMapping("/homepage/team")
    public ApiResponseDto<TeamListDto> teamSearch(@Valid
                                                  @RequestParam(value = "search", defaultValue = "None", required = false) String search,
                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
                                                @RequestParam(value = "sortBy", defaultValue = "teamId", required = false) String sortBy)
    {
        return teamService.getTeamSearchList(pageNo, pageSize, sortBy,search);
    }
    // 홈페이지에서 공모전 검색
    @GetMapping("/homepage/competition")
    public ApiResponseDto<CompetitionSearchTotalDto> competitionSearch(@Valid
                                                  @RequestParam(value = "search", defaultValue = "None", required = false) String search,
                                                                       @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                       @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "competitionId", required = false) String sortBy)
    {
        return teamService.competitionTeamSearch(pageNo,pageSize, sortBy, search);
    }
    // 홈페이지에서 유저검색
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
    // 공모전 좋아요 누르기
    @PatchMapping("/homepage/{competition_id}/heart")
    public ApiResponseDto<SuccessResponse> heartCreate(@Valid
    @AuthenticationPrincipal UserDetails loginUser,
    @PathVariable Long competition_id)
    {
        return heartService.heartCreate(loginUser, competition_id);
    }
    // 공모전 좋아요 취소
    @DeleteMapping("/homepage/{competition_id}/heart")
    public ApiResponseDto<SuccessResponse> heartDelete(@Valid

                                                       @AuthenticationPrincipal UserDetails loginUser,
                                                       @PathVariable Long competition_id)
    {
        return heartService.heartDelete(loginUser, competition_id);
    }
    @PostMapping("/hompage/{competition_id}")
    public ApiResponseDto<SuccessResponse> teamCreate(@Valid @RequestBody TeamCreateDto teamCreateDto
            ,@PathVariable Long competition_id,
                                                      @RequestParam List<Long> teamMember,
                                                      @AuthenticationPrincipal UserDetails loginUser,
                                                      @RequestParam List<Long> positions,
                                                      @RequestParam List<Long> locations) {
        return teamService.teamcreate(teamCreateDto, competition_id, teamMember,loginUser,locations, positions);
    }
    @PatchMapping("/hompage/{competition_id}/{teamId}")
    public ApiResponseDto<SuccessResponse> teamUpdate(@Valid @RequestBody TeamCreateDto teamCreateDto
            ,@PathVariable Long competition_id,
                                                      @RequestParam List<Long> teamMember,
                                                      @AuthenticationPrincipal UserDetails loginUser,
                                                      @RequestParam List<Long> locations,
                                                      @RequestParam List<Long> positions,
                                                      @PathVariable Long teamId) {
        return teamService.teamUpdate(teamCreateDto, competition_id, teamMember,loginUser,locations, positions, teamId);
    }
    @DeleteMapping("/hompage/{competition_id}/{teamId}")
    public ApiResponseDto<SuccessResponse> teamDelete(@Valid
                                                      @PathVariable Long teamId,
                                                      @PathVariable Long competition_id,
                                                      @AuthenticationPrincipal UserDetails loginUser
    ) {
        return teamService.teamdelete(teamId,loginUser, competition_id);
    }
    @GetMapping("/homepage/{competition_id}/competition")
    public ApiResponseDto<CompetitionSearchTotalDto> competitionTeamSearch(@Valid
    @PathVariable Long competition_id,
                                                                       @RequestParam(value = "search", defaultValue = "None", required = false) String keyword,
                                                                       @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                       @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "competitionId", required = false) String sortBy)
    {
        return teamService.competitionTeamSearch(pageNo,pageSize, sortBy, keyword);
    }
    @GetMapping("/homepage/{competition_id}/member")
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



}