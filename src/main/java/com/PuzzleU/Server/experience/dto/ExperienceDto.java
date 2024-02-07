package com.PuzzleU.Server.experience.dto;

import com.PuzzleU.Server.common.enumSet.ExperienceType;
import com.PuzzleU.Server.experience.entity.Experience;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperienceDto {

    private Long ExperienceId;

    @Size(max = 100)
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
