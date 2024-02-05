package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.competition.CompetitionSearchDto;
import com.PuzzleU.Server.dto.team.TeamCreateDto;
import com.PuzzleU.Server.dto.user.SignupRequestDto;
import com.PuzzleU.Server.service.User.UserService;
import com.PuzzleU.Server.service.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/search/{keyword}")
    public ApiResponseDto<List<CompetitionSearchDto>> forteamsearch(@Valid
                                                                    @PathVariable String keyword)
    {
        return teamService.competitionTeamSearch(keyword);
    }
}
