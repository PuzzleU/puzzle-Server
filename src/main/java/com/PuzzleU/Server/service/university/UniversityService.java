package com.PuzzleU.Server.service.university;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.user.UserRegisterOptionalDto;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.repository.MajorRepository;
import com.PuzzleU.Server.repository.UniversityRepository;
import com.PuzzleU.Server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;

    public ApiResponseDto<SuccessResponse> createUniversity(
            Long userId

    )
    {
        // 유저정보 저장해주고
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> {
            System.out.println("User not found");
            return new RestApiException(ErrorType.NOT_MATCHING_INFO);
        });

    }
}
