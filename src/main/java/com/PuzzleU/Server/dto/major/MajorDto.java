package com.PuzzleU.Server.dto.major;

import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorDto {
    private Long majorId;
    private String majorName;
}
