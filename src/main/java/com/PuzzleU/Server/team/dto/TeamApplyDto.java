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
    private TeamAbstractBaseDto teamAbstractDtoWaiting;

    @JsonProperty("End")
    private TeamAbstractBaseDto teamAbstractDtoEnd;
}

