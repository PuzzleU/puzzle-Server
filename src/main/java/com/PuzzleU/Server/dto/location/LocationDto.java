package com.PuzzleU.Server.dto.location;

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
