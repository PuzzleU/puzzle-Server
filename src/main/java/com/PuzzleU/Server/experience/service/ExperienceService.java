package com.PuzzleU.Server.experience.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ErrorResponse;
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
import org.springframework.util.StringUtils;

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
    ) {
        // ExperienceDto의 필드 유효성 검사 추가
        if (experienceDto == null || StringUtils.isEmpty(experienceDto.getExperienceName()) ||
                experienceDto.getExperienceStartYear() == null || experienceDto.getExperienceStartMonth() == null ||
                experienceDto.getExperienceEndYear() == null || experienceDto.getExperienceEndMonth() == null ||
                StringUtils.isEmpty(experienceDto.getExperienceType()) ||
                StringUtils.isEmpty(experienceDto.getExperienceStatus()) ||
                StringUtils.isEmpty(experienceDto.getExperienceRole())) {
            throw new RestApiException(ErrorType.NOT_EXPERIENCE_YET);
        }

        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Experience experience = new Experience(experienceDto.getExperienceName(),experienceDto.getExperienceType(),experienceDto.getExperienceStartYear(),experienceDto.getExperienceStartMonth(),experienceDto.getExperienceEndYear(),experienceDto.getExperienceEndMonth(),experienceDto.getExperienceStatus(),experienceDto.getExperienceRole(),user);
        experienceRepository.save(experience);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "경험 저장완료"), ErrorResponse.builder().status(200).message("요청 성공").build());
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
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "경험 수정완료"), ErrorResponse.builder().status(200).message("요청 성공").build());

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
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "경험 삭제완료"), ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    // 유저가 자신의 페이지에서 자신의 경험리스트들을 확인가능한 API
    public ApiResponseDto<List<ExperienceDto>> getExperienceList(UserDetails loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        List<ExperienceDto> experienceList = new ArrayList<>();
        List<Experience> userExperiences = experienceRepository.findByUser(user);

        if (userExperiences.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND);
        }

        for (Experience experience : userExperiences) {
            ExperienceDto experienceDto = ExperienceDto.of(experience);
            experienceList.add(experienceDto);
        }
        return ResponseUtils.ok(experienceList, ErrorResponse.builder().status(200).message("요청 성공").build());
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
        ExperienceDto experienceDto = ExperienceDto.of(experience);

        return ResponseUtils.ok(experienceDto, ErrorResponse.builder().status(200).message("요청 성공").build());

    }
}
