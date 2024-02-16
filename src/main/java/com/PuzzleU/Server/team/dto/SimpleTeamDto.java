package com.PuzzleU.Server.team.dto;

import com.PuzzleU.Server.location.dto.LocationDto;
import com.PuzzleU.Server.position.dto.PositionDto;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleTeamDto {
    private Long teamId;
    private String teamTitle;
    private String teamWriter;
    private List<PositionDto> positionList;
    private Integer teamNowMember;
    private Integer teamNeed;
    private Boolean teamUntact;
    private List<LocationDto> teamLocations;
    private String CompetitionPoster;
}
