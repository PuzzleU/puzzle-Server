package com.PuzzleU.Server.service.university;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.university.UniversityDto;
import com.PuzzleU.Server.dto.user.UserRegisterOptionalDto;
import com.PuzzleU.Server.dto.user.UserUniversityDto;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.entity.major.Major;
import com.PuzzleU.Server.entity.university.University;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.repository.MajorRepository;
import com.PuzzleU.Server.repository.UniversityRepository;
import com.PuzzleU.Server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public ApiResponseDto<SuccessResponse> createUniversity(
            Long userId, UserUniversityDto userUniversityDto
    )
    {
        // 유저정보 저장해주고
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> {
            System.out.println("User not found");
            return new RestApiException(ErrorType.NOT_MATCHING_INFO);
        });
        // unviersityid로 id값 얻고
        // majorid로 id값 얻어서
        // 한번에 dto에 저장하고 repository에 저장
        Optional<University>universityOptional = universityRepository.findById(userUniversityDto.getUniversityId());
        University university = universityOptional.orElseThrow(()->{
            System.out.println("University not found");
            return new RestApiException(ErrorType.NOT_FOUND_UNIVERSITY);
        });
        Optional<Major>majorOptional = majorRepository.findById(userUniversityDto.getMajorId());
        Major major = majorOptional.orElseThrow(()->{
            System.out.println("Major not found");
            return new RestApiException(ErrorType.NOT_FOUND_MAJOR);
        });
        user.setUniversity(university);
        user.setMajor(major);
        user.setUniversityStart(userUniversityDto.getUserUniversityStart());
        user.setUniversityEnd(userUniversityDto.getUserUniversityEnd());
        user.setUniversityStatus(userUniversityDto.getUniversityStatus());
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "유저학력 저장완료"), null);

    }
}
