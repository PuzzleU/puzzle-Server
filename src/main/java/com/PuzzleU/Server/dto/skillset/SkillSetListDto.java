package com.PuzzleU.Server.dto.skillset;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillSetListDto {
    List<SkillSetDto> skillSetDtoList = new ArrayList<>();
}
