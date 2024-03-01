package com.PuzzleU.Server.team.dto;

import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.position.entity.Position;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Builder
public class TeamAbstractDto extends TeamAbstractBaseDto{
    private Long applyId;

}
