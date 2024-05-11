package com.PuzzleU.Server.common.staticData;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ErrorResponse;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.interest.dto.InterestTypeDto;
import com.PuzzleU.Server.location.dto.LocationDto;
import com.PuzzleU.Server.position.dto.PositionDto;
import com.PuzzleU.Server.profile.dto.ProfileDto;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.location.service.LocationService;
import com.PuzzleU.Server.interest.service.InterestService;
import com.PuzzleU.Server.position.service.PositionService;
import com.PuzzleU.Server.profile.service.ProfileService;
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
                .positionList(positionList)
                .interestList(interestList)
                .locationList(locationList)
                .profileList(profileList).build();

        return ResponseUtils.ok(staticDataDto, ErrorResponse.builder().status(200).message("요청 성공").build());
    }
}
