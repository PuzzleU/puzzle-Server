package com.PuzzleU.Server.team.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TeamAbstractBaseDto {
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
