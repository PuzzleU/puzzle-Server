package com.PuzzleU.Server.university.dto;

import com.PuzzleU.Server.common.enumSet.UniversityStatus;
import com.PuzzleU.Server.common.enumSet.UniversityType;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityRegistDto {

    private Long UniversityId;

    private String UniversityName;

    private com.PuzzleU.Server.common.enumSet.UniversityStatus UniversityStatus;

    private Integer UniversityStart;

    private Integer UniversityEnd;

    private Long MajorId;

    private String MajorName;

    private UniversityType universityType;

}
