package com.PuzzleU.Server.university.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ErrorResponse;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.UniversityType;
import com.PuzzleU.Server.major.dto.MajorSearchDto;
import com.PuzzleU.Server.relations.entity.UserUniversityRelation;
import com.PuzzleU.Server.relations.repository.UserUniversityRelationRepository;
import com.PuzzleU.Server.university.dto.UniversityListDto;
import com.PuzzleU.Server.university.dto.UniversityRegistDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    private final UserUniversityRelationRepository userUniversityRelationRepository;

    // 유저가 본인의 대학을 등록할 수 있는 API
    @Transactional
    public ApiResponseDto<SuccessResponse> createUniversity(
            UserDetails loginUser, UniversityRegistDto universityRegistDto
    )
    {
        // 유저정보 저장해주고
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        // unviersityid로 id값 얻고
        // majorid로 id값 얻어서
        // 한번에 dto에 저장하고 repository에 저장
        Optional<University>universityOptional = universityRepository.findById(universityRegistDto.getUniversityId());
        University university = universityOptional.orElseThrow(()->{
            System.out.println("University not found");
            return new RestApiException(ErrorType.NOT_FOUND_UNIVERSITY);
        });
        Optional<Major>majorOptional = majorRepository.findById(universityRegistDto.getMajorId());
        Major major = majorOptional.orElseThrow(()->{
            System.out.println("Major not found");
            return new RestApiException(ErrorType.NOT_FOUND_MAJOR);
        });
        UserUniversityRelation userUniversityRelation = new UserUniversityRelation();
        userUniversityRelation.setUniversity(university);
        userUniversityRelation.setUserUniversityId(university.getUniversityId());
        userUniversityRelation.setUniversityStatus(universityRegistDto.getUniversityStatus());
        userUniversityRelation.setUniversityEnd(universityRegistDto.getUniversityEnd());
        userUniversityRelation.setUniversityStart(universityRegistDto.getUniversityStart());
        userUniversityRelation.setMajorId(major.getMajorId());
        userUniversityRelation.setMajorName(major.getMajorName());
        userUniversityRelation.setUser(user);
        userUniversityRelationRepository.save(userUniversityRelation);


        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "유저학력 저장완료"), ErrorResponse.builder().status(200).message("요청 성공").build());

    }

    // 대학교 검색 API
    @Transactional
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

        return ResponseUtils.ok(universitySearchTotalDto, ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    @Transactional
    public ApiResponseDto<List<UniversityListDto>> getUniversityList(UserDetails loginUser)
    {
        List<UniversityListDto> universityListDtos = new ArrayList<>();
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        List<UserUniversityRelation> userUniversityRelationList = userUniversityRelationRepository.findByUser(user);
        for(UserUniversityRelation userUniversityRelation : userUniversityRelationList)
        {
            UniversityListDto universityListDto = getUniversityListDto(userUniversityRelation);
            universityListDtos.add(universityListDto);
        }
        return ResponseUtils.ok(universityListDtos, ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    private static UniversityListDto getUniversityListDto(UserUniversityRelation userUniversityRelation) {
        UniversityListDto universityListDto = new UniversityListDto();
        universityListDto.setUniversityId(userUniversityRelation.getUserUniversityId());
        universityListDto.setUniversityName(userUniversityRelation.getUniversity().getUniversityName());
        universityListDto.setUniversityEnd(userUniversityRelation.getUniversityEnd());
        universityListDto.setUniversityStart(userUniversityRelation.getUniversityStart());
        universityListDto.setMajorId(userUniversityRelation.getMajorId());
        universityListDto.setMajorName(userUniversityRelation.getMajorName());
        return universityListDto;
    }

}
