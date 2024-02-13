package com.PuzzleU.Server.relations.dto;

import com.PuzzleU.Server.common.enumSet.Level;
import lombok.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillsetRelationListDto {
    private Long userSkillsetRelationId;
    private Long skillsetId;
    private Level level;
}
