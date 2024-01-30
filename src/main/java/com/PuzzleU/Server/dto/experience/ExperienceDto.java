package com.PuzzleU.Server.dto.experience;

import com.PuzzleU.Server.entity.enumSet.ExperienceType;
import com.PuzzleU.Server.entity.experience.Experience;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienceDto {

    private String ExperienceName;

    private ExperienceType ExperienceType;

    private Integer ExperienceStartYear;

    private Integer ExperienceEndYear;

    private Integer ExperienceStartMonth;

    private Integer ExperienceEndMonth;

    private Boolean ExperienceStatus;

    private String ExperienceRole;

    public static ExperienceDto of(Experience experience)
    {
        return ExperienceDto.builder()
                .ExperienceName(experience.getExperienceName())
                .ExperienceType(experience.getExperienceType())
                .ExperienceStartYear(experience.getExperienceStartYear())
                .ExperienceStartMonth(experience.getExperienceStartMonth())
                .ExperienceEndYear(experience.getExperienceEndYear())
                .ExperienceEndMonth(experience.getExperienceEndMonth())
                .ExperienceType(experience.getExperienceType())
                .ExperienceRole(experience.getExperienceRole())
                .build();
    }

}
