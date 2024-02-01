package com.PuzzleU.Server.service.profile;

import com.PuzzleU.Server.dto.profile.ProfileDto;
import com.PuzzleU.Server.entity.profile.Profile;
import com.PuzzleU.Server.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    // profile list: 전체 profile list 반환
    public List<ProfileDto> listAllProfiles() {
        List<Profile> profileList = profileRepository.findAll();

        return profileList.stream()
                .map(profile -> ProfileDto.builder()
                        .ProfileId(profile.getProfileId())
                        .ProfileUrl(profile.getProfielUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
