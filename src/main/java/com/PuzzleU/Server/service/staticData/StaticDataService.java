package com.PuzzleU.Server.service.staticData;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.dto.interest.InterestTypeDto;
import com.PuzzleU.Server.dto.location.LocationDto;
import com.PuzzleU.Server.dto.position.PositionDto;
import com.PuzzleU.Server.dto.profile.ProfileDto;
import com.PuzzleU.Server.dto.staticData.StaticDataDto;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.service.location.LocationService;
import com.PuzzleU.Server.service.interest.InterestService;
import com.PuzzleU.Server.service.position.PositionService;
import com.PuzzleU.Server.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaticDataService {
    private final PositionService positionService;
    private final InterestService interestService;
    private final LocationService locationService;
    private final ProfileService profileService;

    // 전체 static data 반환
    public ApiResponseDto<StaticDataDto> getAllStaticData() {

        List<PositionDto> positionList = positionService.listAllPositions();
        if (positionList.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_POSITION_LIST);
        }

        List<InterestTypeDto> interestList = interestService.listAllInterest();
        if (interestList.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_INTEREST_LIST);
        }

        List<LocationDto> locationList = locationService.listAllLocations();
        if (locationList.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_LOCATION_LIST);
        }

        List<ProfileDto> profileList = profileService.listAllProfiles();
        if (profileList.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_PROFILE_LIST);
        }

        StaticDataDto staticDataDto = StaticDataDto.builder()
                .postionList(positionList)
                .interestList(interestList)
                .locationList(locationList)
                .profileList(profileList).build();

        return ResponseUtils.ok(staticDataDto, null);
    }
}
