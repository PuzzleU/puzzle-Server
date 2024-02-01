package com.PuzzleU.Server.service.position;

import com.PuzzleU.Server.dto.position.PositionDto;
import com.PuzzleU.Server.entity.position.Position;
import com.PuzzleU.Server.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;

    // position list: 전체 position list 반환
    public List<PositionDto> listAllPositions() {
        List<Position> positionList = positionRepository.findAll();

        return positionList.stream()
                .map(position -> PositionDto.builder()
                        .PositionId(position.getPositionId())
                        .PositionName(position.getPositionName())
                        .build())
                .collect(Collectors.toList());
    }

}
