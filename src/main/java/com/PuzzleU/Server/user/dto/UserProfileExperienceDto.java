package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.ExperienceType;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileExperienceDto {
    private Long ExperienceId;
    private String ExperienceName;
    private ExperienceType ExperienceType;
}
