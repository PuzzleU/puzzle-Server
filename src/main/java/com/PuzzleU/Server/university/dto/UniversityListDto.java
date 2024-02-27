package com.PuzzleU.Server.university.dto;

import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityListDto {
    private Long universityId;
    private String universityName;
    private Long majorId;
    private String majorName;
    private Integer universityStart;
    private Integer universityEnd;
}
