package com.PuzzleU.Server.common.staticData;

import com.PuzzleU.Server.interest.dto.InterestTypeDto;
import com.PuzzleU.Server.location.dto.LocationDto;
import com.PuzzleU.Server.position.dto.PositionDto;
import com.PuzzleU.Server.profile.dto.ProfileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaticDataDto {
    private List<PositionDto> positionList;
    private List<InterestTypeDto> interestList;
    private List<LocationDto> locationList;
    private List<ProfileDto> profileList;
}
