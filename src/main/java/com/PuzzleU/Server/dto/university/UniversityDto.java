package com.PuzzleU.Server.dto.university;

import com.PuzzleU.Server.dto.major.MajorDto;
import com.PuzzleU.Server.entity.enumSet.UniversityType;
import com.PuzzleU.Server.entity.major.Major;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityDto {
    private Long UniversityId;
    @Size(max = 15)
    private UniversityType universityType;
    private String universityName;
}
