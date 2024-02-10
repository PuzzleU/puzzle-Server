package com.PuzzleU.Server.team.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyTeamDto {
    @JsonProperty("WAITING")
    private TeamAbstractDto teamListDto1;
    @JsonProperty("FINISHED")
    private TeamAbstractDto teamListDto2;
}
