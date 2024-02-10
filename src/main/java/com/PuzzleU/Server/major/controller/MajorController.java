package com.PuzzleU.Server.major.controller;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.major.dto.MajorSearchTotalDto;
import com.PuzzleU.Server.major.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MajorController {

    private final MajorService majorService;
    @GetMapping("/university/search/{universityId}")
    public ApiResponseDto<MajorSearchTotalDto> searchMajorList(
            @PathVariable("universityId") Long universityId,
            @RequestParam(value = "word", defaultValue = "None", required = false) String searchKeyword,
            @PageableDefault(size = 6, sort = "universityName", direction = Sort.Direction.ASC) Pageable pageable) {
        return majorService.searchMajorList(universityId, searchKeyword, pageable);
    }
}
