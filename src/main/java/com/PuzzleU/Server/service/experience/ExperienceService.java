package com.PuzzleU.Server.service.experience;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.experience.ExperienceDto;
import com.PuzzleU.Server.dto.user.UserRegisterOptionalDto;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.entity.experience.Experience;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.repository.ExperienceRepository;
import com.PuzzleU.Server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;


    public ApiResponseDto<SuccessResponse> createExperience(
            Long userId,
            ExperienceDto experienceDto
    )
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> {
            System.out.println("User not found");
            return new RestApiException(ErrorType.NOT_MATCHING_INFO);
        });
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
    public ApiResponseDto<SuccessResponse> updateExperience(
            Long userId,
            Long experienceId,
            ExperienceDto experienceDto
    )
    {
        Optional<Experience> optionalExperience = experienceRepository.findExperienceIdAndUserId(experienceId,userId);
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
    public ApiResponseDto<SuccessResponse> deleteExperience(
            Long userId,Long experienceId
    )
    {
        Optional<Experience> optionalExperience = experienceRepository.findExperienceIdAndUserId(experienceId,userId);
        Experience experience = optionalExperience.orElseThrow(() -> {
            System.out.println("User not found");
            return new RestApiException(ErrorType.NOT_FOUND_EXPERIENCE);
        });
        experienceRepository.delete(experience);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "경험 삭제완료"), null);
    }

}
