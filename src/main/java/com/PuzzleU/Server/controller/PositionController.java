package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.dto.position.PositionDto;
import com.PuzzleU.Server.service.Position.PositionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/position")
public class PositionController {
    final private PositionService positionService;
    @GetMapping("/list")
    public List<PositionDto> listAllPositions() {
        return positionService.listAllPositions();
    }
}
