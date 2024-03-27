package com.PuzzleU.Server.position.dto;

import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionDto {
    private Long PositionId;
    private String PositionName;
    private String PositionUrl;
}
