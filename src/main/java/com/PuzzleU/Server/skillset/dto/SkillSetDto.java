package com.PuzzleU.Server.skillset.dto;

import com.PuzzleU.Server.common.enumSet.Level;
import lombok.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillSetDto {

    private Long SkillSetId;

    private Level SkillSetLevel;

}
