package com.PuzzleU.Server.dto.staticData;

import com.PuzzleU.Server.dto.interest.InterestTypeDto;
import com.PuzzleU.Server.dto.location.LocationDto;
import com.PuzzleU.Server.dto.position.PositionDto;
import com.PuzzleU.Server.dto.profile.ProfileDto;
import lombok.*;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaticDataDto {
    private List<PositionDto> postionList;
    private List<InterestTypeDto> interestList;
    private List<LocationDto> locationList;
    private List<ProfileDto> profileList;
}
