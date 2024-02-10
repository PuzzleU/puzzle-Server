package com.PuzzleU.Server.team.dto;

import com.PuzzleU.Server.user.dto.UserSimpleDto;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSpecificDto {
    private String teamTitle;
    private String teamWriter;
    private List<String> positionList;
    private Integer teamNowMember;
    private Integer teamNeed;
    private Boolean teamUntact;
    private List<String> teamLocations;
    private String teamPoster;
    private List<UserSimpleDto> members;
    private String teamIntroduce;
    private String teamContent;

}
