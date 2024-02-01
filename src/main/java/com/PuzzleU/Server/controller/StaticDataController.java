package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.dto.staticData.StaticDataDto;
import com.PuzzleU.Server.service.staticData.StaticDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Static Data
    : 바뀌지 않는 데이터

    -   Position
    -   Interest
    -   Location
    -   Profile

    => splash page에서 전달하는 데이터들
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/splash")
public class StaticDataController {
    private final StaticDataService staticDataService;

    @GetMapping("")
    public ApiResponseDto<StaticDataDto> getAllStaticData() {
        return staticDataService.getAllStaticData();
    }


}
