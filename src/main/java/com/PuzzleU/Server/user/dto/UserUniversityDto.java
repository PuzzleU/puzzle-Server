package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.UniversityStatus;
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
