package com.PuzzleU.Server.dto.skillset;

import com.PuzzleU.Server.entity.enumSet.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillSetDto {

    private Long SkillSetId;

    private Level SkillSetLevel;

}
