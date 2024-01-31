package com.PuzzleU.Server.dto.user;

import com.PuzzleU.Server.dto.experience.ExperienceDto;
import com.PuzzleU.Server.dto.skillset.SkillSetDto;
import com.PuzzleU.Server.entity.enumSet.UniversityStatus;
import com.PuzzleU.Server.entity.enumSet.WorkType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserRegisterOptionalDto {


    private UniversityStatus UniversityStatus;

    private Integer UniversityStart;

    private Integer UniversityEnd;

    private Long UniversityId;

    private Long MajorId;

    private String UserNudge;

    private WorkType UserWorkType;

    private List<ExperienceDto> experienceDtoList;

    private List<SkillSetDto> skillSetDtoList;

}
