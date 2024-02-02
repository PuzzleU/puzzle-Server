package com.PuzzleU.Server.dto.university;

import com.PuzzleU.Server.dto.major.MajorDto;
import com.PuzzleU.Server.entity.enumSet.UniversityType;
import com.PuzzleU.Server.entity.major.Major;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityDto {
    private Long UniversityId;
    private UniversityType universityType;
    private String universityName;
}
