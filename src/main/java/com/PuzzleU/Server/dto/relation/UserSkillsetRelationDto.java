package com.PuzzleU.Server.dto.relation;

import com.PuzzleU.Server.entity.enumSet.Level;
import com.PuzzleU.Server.entity.skillset.Skillset;
import com.PuzzleU.Server.entity.user.User;
import lombok.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillsetRelationDto {
    private Long userId;
    private Long skillsetId;
    private Level level;
}
