package com.PuzzleU.Server.team.dto;

import com.PuzzleU.Server.competition.entity.Competition;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamCreateDto {
    private Competition competition;
    @Size(max = 30)
    private String teamTitle;

    private Integer teamMemberNow;

    private Integer teamMemberNeed;

    private Boolean teamUntact;

    @Size(max = 200)
    private String teamUrl;

    @Size(max = 300)
    private String teamIntroduce;

    @Size(max = 500)
    private String teamContent;

    private Boolean teamStatus;

}

