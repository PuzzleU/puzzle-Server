package com.PuzzleU.Server.skillset.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@Builder
@AllArgsConstructor
public class SkillSetListDto {
    @Builder.Default
    List<SkillSetDto> skillSetDtoList = new ArrayList<>();
}
