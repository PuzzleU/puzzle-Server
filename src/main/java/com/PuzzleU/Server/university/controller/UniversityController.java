package com.PuzzleU.Server.university.controller;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.university.dto.UniversityDto;
import com.PuzzleU.Server.university.dto.UniversitySearchTotalDto;
import com.PuzzleU.Server.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/university")
public class UniversityController {
    private final UniversityService universityService;

    @GetMapping("/search")
    public ApiResponseDto<UniversitySearchTotalDto> searchUniversity(
            @RequestParam(value = "word", defaultValue = "None", required = false) String searchKeyword,
            @RequestParam(value = "type", defaultValue = "UNIVERSITY", required = true) String type,
            @PageableDefault(size = 6, sort = "universityName", direction = Sort.Direction.ASC) Pageable pageable) {
        return universityService.searchUniversityList(searchKeyword, type, pageable);
    }
}
