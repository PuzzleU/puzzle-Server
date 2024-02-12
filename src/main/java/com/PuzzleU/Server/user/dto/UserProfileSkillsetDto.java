package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.Level;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileSkillsetDto {
    private Long SkillsetId;
    private String SkillsetName;
    private String SkillsetLogo;
    private Level SKillsetLevel;

}
