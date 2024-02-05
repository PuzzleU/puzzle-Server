package com.PuzzleU.Server.dto.competition;

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

}
