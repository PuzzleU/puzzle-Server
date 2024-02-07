package com.PuzzleU.Server.location.dto;

import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    private Long LocationId;
    private String LocationName;
}
