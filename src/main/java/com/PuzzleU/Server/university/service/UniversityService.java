package com.PuzzleU.Server.university.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.UniversityType;
import com.PuzzleU.Server.major.dto.MajorSearchDto;
import com.PuzzleU.Server.university.dto.UniversitySearchDto;
import com.PuzzleU.Server.university.dto.UniversitySearchTotalDto;
import com.PuzzleU.Server.university.entity.University;
import com.PuzzleU.Server.university.repository.UniversityRepository;
import com.PuzzleU.Server.user.dto.UserUniversityDto;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.major.entity.Major;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.major.repository.MajorRepository;
import com.PuzzleU.Server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;

    // 유저가 본인의 대학을 등록할 수 있는 API
    @Transactional
    public ApiResponseDto<SuccessResponse> createUniversity(
            UserDetails loginUser, UserUniversityDto userUniversityDto
    )
    {
        // 유저정보 저장해주고
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

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

    // 대학교 검색 API
    public ApiResponseDto<UniversitySearchTotalDto> searchUniversityList(String searchKeyword, String type, Pageable pageable) {
        UniversityType universityType = UniversityType.UNIVERSITY;

        System.out.println("universityType: " + type);

        if (type.equals("UNIVERSITY")) {
            universityType = UniversityType.UNIVERSITY;
        }
        else if (type.equals("GRADUATE")) {
            universityType = UniversityType.GRADUATE;
        }
        else {
            throw new RestApiException(ErrorType.NOT_MATCHING_UNIVERSITY_TYPE);
        }

        Page<University> universityPage = universityRepository.findByUniversityNameContainingAndUniversityType(searchKeyword, universityType, pageable);
        Page<UniversitySearchDto> universitySearchDtoPage = universityPage.map(u
                -> new UniversitySearchDto(u.getUniversityId(), u.getUniversityName()));


        List<UniversitySearchDto> universitySearchList = universitySearchDtoPage.getContent();

        UniversitySearchTotalDto universitySearchTotalDto = UniversitySearchTotalDto.builder()
                .UniversityList(universitySearchList)
                .UniversityType(universityType)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(universityPage.getTotalElements())
                .totalPages(universityPage.getTotalPages())
                .last(universityPage.isLast()).build();

        return ResponseUtils.ok(universitySearchTotalDto, null);
    }
}
