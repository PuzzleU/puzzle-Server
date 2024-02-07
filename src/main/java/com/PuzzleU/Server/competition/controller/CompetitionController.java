package com.PuzzleU.Server.competition.controller;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.competition.dto.CompetitionHomeTotalDto;
import com.PuzzleU.Server.competition.dto.CompetitionSpecificDto;
import com.PuzzleU.Server.common.enumSet.CompetitionType;
import com.PuzzleU.Server.competition.service.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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


}