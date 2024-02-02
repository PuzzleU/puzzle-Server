package com.PuzzleU.Server.dto.position;

import com.PuzzleU.Server.entity.enumSet.Level;
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
}
