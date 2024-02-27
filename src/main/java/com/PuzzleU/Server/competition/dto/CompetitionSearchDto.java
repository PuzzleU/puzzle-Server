package com.PuzzleU.Server.competition.dto;

import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionSearchDto {

    private String competitionName;
    private Long competitionId;
    private String competitionPoster;

}
