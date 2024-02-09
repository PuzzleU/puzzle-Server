package com.PuzzleU.Server.university.dto;

import com.PuzzleU.Server.common.enumSet.UniversityType;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversitySearchDto {
    private Long UniversityId;
    private String universityName;
}
