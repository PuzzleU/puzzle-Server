package com.PuzzleU.Server.location.service;

import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.location.dto.LocationDto;
import com.PuzzleU.Server.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    // location list: 전체 location list 반환
    public List<LocationDto> listAllLocations() {
        List<Location> locationList = locationRepository.findAll();

        return locationList.stream()
                .map(location -> LocationDto.builder()
                        .LocationId(location.getLocationId())
                        .LocationName(location.getLocationName())
                        .build())
                .collect(Collectors.toList());
    }
}
