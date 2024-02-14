package com.PuzzleU.Server.major.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.major.dto.MajorSearchDto;
import com.PuzzleU.Server.major.dto.MajorSearchTotalDto;
import com.PuzzleU.Server.major.entity.Major;
import com.PuzzleU.Server.major.repository.MajorRepository;
import com.PuzzleU.Server.university.entity.University;
import com.PuzzleU.Server.university.repository.UniversityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;
    private final UniversityRepository universityRepository;


    // 대학교에 대한 전공 검색 API
    @Transactional
    public ApiResponseDto<MajorSearchTotalDto> searchMajorList(Long universityId, String searchKeyword, Pageable pageable) {
        University university = universityRepository.findByUniversityId(universityId)
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_UNIVERSITY));

        /*
        List<Major> majorPage = majorRepository.findByMajorNameContainingAndUniversityId(searchKeyword, universityId, pageable);

        Page<MajorSearchDto> majorSearchDtoPage = majorPage.map(m
            -> new MajorSearchDto(m.getMajorId(), m.getMajorName()));

        List<MajorSearchDto> majorSearchList = majorSearchDtoPage.getContent();

        MajorSearchTotalDto majorSearchTotalDto = MajorSearchTotalDto.builder()
                .MajorList(majorSearchList)
                .UniversityId(university.getUniversityId())
                .UniversityName(university.getUniversityName())
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(majorPage.getTotalElements())
                .totalPages(majorPage.getTotalPages())
                .last(majorPage.isLast()).build();

        return ResponseUtils.ok(majorSearchTotalDto, null);
         */

        List<Major> majorList = majorRepository.findByUniversityAndMajorNameContaining(university, searchKeyword);

        List<MajorSearchDto> majorSearchDtoList = majorList.stream().map(
                major -> {
                    MajorSearchDto majorSearchDto = new MajorSearchDto();
                    majorSearchDto.setMajorId(major.getMajorId());
                    majorSearchDto.setMajorName(major.getMajorName());
                    return majorSearchDto;
                }).collect(Collectors.toList());

        MajorSearchTotalDto majorSearchTotalDto = MajorSearchTotalDto.builder()
                .MajorList(majorSearchDtoList).build();

        return ResponseUtils.ok(majorSearchTotalDto, null);
    }
}
