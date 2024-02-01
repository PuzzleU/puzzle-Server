package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.dto.staticData.StaticDataDto;
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
    @GetMapping("")
    public ApiResponseDto<StaticDataDto> getAllStaticData() {
    }


}
