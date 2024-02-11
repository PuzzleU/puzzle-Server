package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.UniversityStatus;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUniversityDto {
    private Long UniversityId;
    private String UniversityName;
    private Long MajorId;
    private String MajorName;
    private UniversityStatus UserUniversityStatus;
    private Integer UserUniversityStart;
    private Integer UserUniversityEnd;
}
