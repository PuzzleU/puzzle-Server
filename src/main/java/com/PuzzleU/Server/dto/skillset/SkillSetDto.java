package com.PuzzleU.Server.dto.skillset;

import com.PuzzleU.Server.entity.enumSet.Level;
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
