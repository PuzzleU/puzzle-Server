package com.PuzzleU.Server.experience.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.experience.dto.ExperienceDto;
import com.PuzzleU.Server.experience.entity.Experience;
import com.PuzzleU.Server.experience.repository.ExperienceRepository;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;



    // 유저의 경험을 생성하는 API
    public ApiResponseDto<SuccessResponse> createExperience(
            UserDetails loginUser,
            ExperienceDto experienceDto
    )
    {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Experience experience = new Experience();
        experience.setUser(user);
        experience.setExperienceName(experienceDto.getExperienceName());
        experience.setExperienceStartYear(experienceDto.getExperienceStartYear());
        experience.setExperienceStartMonth(experienceDto.getExperienceStartMonth());
        experience.setExperienceEndYear(experienceDto.getExperienceEndYear());
        experience.setExperienceEndMonth(experienceDto.getExperienceEndMonth());
        experience.setExperienceType(experienceDto.getExperienceType());
        experience.setExperienceStatus(experienceDto.getExperienceStatus());
        experience.setExperienceRole(experienceDto.getExperienceRole());
        experienceRepository.save(experience);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "경험 저장완료"), null);

    }
    // 유저의 경험을 수정하는 API
    public ApiResponseDto<SuccessResponse> updateExperience(
            UserDetails loginUser,
            Long experienceId,
            ExperienceDto experienceDto
    )
    {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Experience> optionalExperience = experienceRepository.findByExperienceIdAndUser(experienceId,user);
        Experience experience = optionalExperience.orElseThrow(() -> {
            System.out.println("User not found");
            return new RestApiException(ErrorType.NOT_FOUND_EXPERIENCE);
        });

        if (experienceDto.getExperienceName() != null) {
            experience.setExperienceName(experienceDto.getExperienceName());
        }

        if (experienceDto.getExperienceStartYear() != null) {
            experience.setExperienceStartYear(experienceDto.getExperienceStartYear());
        }

        if (experienceDto.getExperienceStartMonth() != null) {
            experience.setExperienceStartMonth(experienceDto.getExperienceStartMonth());
        }

        if (experienceDto.getExperienceEndYear() != null) {
            experience.setExperienceEndYear(experienceDto.getExperienceEndYear());
        }

        if (experienceDto.getExperienceEndMonth() != null) {
            experience.setExperienceEndMonth(experienceDto.getExperienceEndMonth());
        }

        if (experienceDto.getExperienceType() != null) {
            experience.setExperienceType(experienceDto.getExperienceType());
        }

        if (experienceDto.getExperienceStatus() != null) {
            experience.setExperienceStatus(experienceDto.getExperienceStatus());
        }

        if (experienceDto.getExperienceRole() != null) {
            experience.setExperienceRole(experienceDto.getExperienceRole());
        }

        experienceRepository.save(experience);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "경험 수정완료"), null);

    }
    // 유저의 경험을 삭제하는 API
    public ApiResponseDto<SuccessResponse> deleteExperience(
            UserDetails loginUser,Long experienceId
    )
    {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Experience> optionalExperience = experienceRepository.findByExperienceIdAndUser(experienceId,user);
        Experience experience = optionalExperience.orElseThrow(() -> {
            System.out.println("User not found");
            return new RestApiException(ErrorType.NOT_FOUND_EXPERIENCE);
        });
        experienceRepository.delete(experience);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "경험 삭제완료"), null);
    }

    // 유저가 자신의 페이지에서 자신의 경험리스트들을 확인가능한 API
    public ApiResponseDto<List<ExperienceDto>> getExperienceList(UserDetails loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        List<ExperienceDto> experienceList = new ArrayList<>();
        List<Experience> userExperiences = experienceRepository.findByUser(user);
        System.out.println(userExperiences);
        for (Experience experience : userExperiences) {
            ExperienceDto experienceDto = new ExperienceDto();
            experienceDto.setExperienceId(experience.getExperienceId());
            experienceDto.setExperienceName(experience.getExperienceName());
            experienceDto.setExperienceStartYear(experience.getExperienceStartYear());
            experienceDto.setExperienceStartMonth(experience.getExperienceStartMonth());
            experienceDto.setExperienceEndYear(experience.getExperienceEndYear());
            experienceDto.setExperienceEndMonth(experience.getExperienceEndMonth());
            experienceDto.setExperienceType(experience.getExperienceType());
            experienceDto.setExperienceStatus(experience.getExperienceStatus());
            experienceDto.setExperienceRole(experience.getExperienceRole());
            experienceList.add(experienceDto);
        }
        return ResponseUtils.ok(experienceList, null);

    }
    // 유저가 경험 리스트들중 하나를 클릭하면 자신의 세부적인 경험을 확인할 수 있는 API
    public ApiResponseDto<ExperienceDto> getExperience(UserDetails loginUser, Long experienceId) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Experience> optionalExperience = experienceRepository.findByExperienceIdAndUser(experienceId,user);
        Experience experience = optionalExperience.orElseThrow(() -> {
            System.out.println("User not found");
            return new RestApiException(ErrorType.NOT_FOUND_EXPERIENCE);
        });
        ExperienceDto experienceDto = new ExperienceDto();
        experienceDto.setExperienceId(experience.getExperienceId());
        experienceDto.setExperienceName(experience.getExperienceName());
        experienceDto.setExperienceStartYear(experience.getExperienceStartYear());
        experienceDto.setExperienceStartMonth(experience.getExperienceStartMonth());
        experienceDto.setExperienceEndYear(experience.getExperienceEndYear());
        experienceDto.setExperienceEndMonth(experience.getExperienceEndMonth());
        experienceDto.setExperienceType(experience.getExperienceType());
        experienceDto.setExperienceStatus(experience.getExperienceStatus());
        experienceDto.setExperienceRole(experience.getExperienceRole());


        return ResponseUtils.ok(experienceDto, null);

    }
}
