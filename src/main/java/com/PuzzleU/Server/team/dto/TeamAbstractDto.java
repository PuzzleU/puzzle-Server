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
public class TeamAbstractDto {

    private Long teamId;
    private String teamTitle;
    private String teamWriter;
    private List<String> positionList;
    private Integer teamNowMember;
    private Integer teamNeed;
    private Boolean teamUntact;
    private List<String> teamLocations;
    private String teamPoster;
}
