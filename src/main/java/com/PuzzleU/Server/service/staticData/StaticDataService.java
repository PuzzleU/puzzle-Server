package com.PuzzleU.Server.service.staticData;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.dto.staticData.StaticDataDto;
import com.PuzzleU.Server.repository.PositionRepository;
import com.PuzzleU.Server.service.LocationService;
import com.PuzzleU.Server.service.interest.InterestService;
import com.PuzzleU.Server.service.position.PositionService;
import com.PuzzleU.Server.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaticDataService {
    private final PositionService positionService;
    private final InterestService interestService;
    private final LocationService locationService;
    private final ProfileService profileService;

    // 전체 static data 반환
    public ApiResponseDto<StaticDataDto> getAllStaticData() {
        StaticDataDto staticDataDto = StaticDataDto.builder()
                .postionList(positionService.listAllPositions())
                .interestList(interestService.listAllInterest())
                .locationList(locationService.listAllLocations())
                .profileList(profileService.listAllProfiles()).build();

        return ResponseUtils.ok(staticDataDto, null);
    }
}
