package com.PuzzleU.Server.team.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamApplyDto {

    @JsonProperty("Waiting")
    private TeamAbstractDto teamAbstractDtoWaiting;

    @JsonProperty("End")
    private TeamAbstractDto teamAbstractDtoEnd;
}

