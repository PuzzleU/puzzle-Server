package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.experience.dto.ExperienceDto;
import com.PuzzleU.Server.skillset.dto.SkillSetDto;
import com.PuzzleU.Server.common.enumSet.UniversityStatus;
import com.PuzzleU.Server.common.enumSet.WorkType;
import com.PuzzleU.Server.university.dto.UniversityRegistDto;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserRegisterOptionalDto {


    private List<UniversityRegistDto> universityRegistDtoList;


    @Size(max=100)
    private String UserRepresentativeExperience;
    @Size(max=100)
    private String UserRepresentativeProfileSentence;

    private WorkType UserWorkType;

    private List<ExperienceDto> experienceDtoList;

    private List<SkillSetDto> skillSetDtoList;

}
