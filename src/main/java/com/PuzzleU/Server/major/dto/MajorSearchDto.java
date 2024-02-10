package com.PuzzleU.Server.major.dto;

import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorSearchDto {
    private Long MajorId;
    private String MajorName;
}
