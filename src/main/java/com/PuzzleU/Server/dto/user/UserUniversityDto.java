package com.PuzzleU.Server.dto.user;

import com.PuzzleU.Server.dto.major.MajorDto;
import com.PuzzleU.Server.dto.university.UniversityDto;
import com.PuzzleU.Server.entity.enumSet.UniversityStatus;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUniversityDto {
    private UniversityStatus universityStatus;
    private Integer userUniversityStart;
    private Integer userUniversityEnd;

    private Long universityId;

    private Long majorId;
}
