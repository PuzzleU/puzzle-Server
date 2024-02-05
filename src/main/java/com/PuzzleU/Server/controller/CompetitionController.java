package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.competition.CompetitionDto;
import com.PuzzleU.Server.dto.competition.CompetitionHomeTotalDto;
import com.PuzzleU.Server.dto.user.SignupRequestDto;
import com.PuzzleU.Server.entity.enumSet.CompetitionType;
import com.PuzzleU.Server.service.competition.CompetitionService;
import com.fasterxml.jackson.databind.JsonSerializer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    @GetMapping("/homepage")
    public ApiResponseDto<CompetitionHomeTotalDto> homepage(
            @RequestParam(value = "competitionType", required = false) CompetitionType competitionType,
            @RequestParam(value = "search", defaultValue = "None", required = false) String search,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
    ) {
        return competitionService.getHomepage(pageNo, pageSize, sortBy, search, competitionType);
    }
}