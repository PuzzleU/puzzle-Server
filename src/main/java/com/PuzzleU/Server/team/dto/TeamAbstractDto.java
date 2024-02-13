package com.PuzzleU.Server.team.dto;

import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.position.entity.Position;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamAbstractDto extends TeamAbstractBaseDto{

    private Long applyId;

}
