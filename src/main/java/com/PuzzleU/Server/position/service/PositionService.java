package com.PuzzleU.Server.position.service;

import com.PuzzleU.Server.position.dto.PositionDto;
import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.position.repository.PositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;

    // position list: 전체 position list 반환
    @Transactional
    public List<PositionDto> listAllPositions() {
        List<Position> positionList = positionRepository.findAll();

        return positionList.stream()
                .map(position -> PositionDto.builder()
                        .PositionId(position.getPositionId())
                        .PositionName(position.getPositionName())
                        .PositionUrl(position.getPositionUrl())
                        .build())
                .collect(Collectors.toList());
    }

}
