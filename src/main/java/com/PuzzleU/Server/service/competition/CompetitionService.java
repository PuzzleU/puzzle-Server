package com.PuzzleU.Server.service.competition;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.dto.competition.*;
import com.PuzzleU.Server.entity.competition.Competition;
import com.PuzzleU.Server.entity.enumSet.CompetitionType;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.repository.CompetitionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;

    private final Logger logger = LoggerFactory.getLogger(CompetitionService.class);

    @Transactional
    public ApiResponseDto<CompetitionHomeTotalDto> getHomepage(int pageNo, int pageSize, String sortBy, String keyword, CompetitionType type) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Competition> competitionPage;
        String None = "None";
        // 키워드와 타입이 모두 제공
        // 안됨
        if (!keyword.equals(None) && type != null) {
            System.out.println("1");
            competitionPage = new PageImpl<>(competitionRepository.findByKeywordAndType(keyword, type));
        }
        // 키워드는 없고 타입만 제공될 때
        //안됨
        else if (keyword.equals(None) && type !=null) {
            System.out.println("2");
            competitionPage = new PageImpl<>(competitionRepository.findByType(type, pageable));
        }
        // 키워드만 존재할 때
        // 잘됨
        else if (!keyword.equals(None) && type == null) {
            System.out.println("3");
            competitionPage = new PageImpl<>(competitionRepository.findByKeyword(keyword, pageable));
        }
        // 키워드와 타입 둘 다 없을 때
        // 잘됨
        else {
            System.out.println("4");
            competitionPage = competitionRepository.findAll(pageable);
        }

        List<CompetitionHomePageDto> competitionHomePageDtos = competitionPage.getContent().stream()
                .map(competition -> {
                    CompetitionHomePageDto competitionHomePageDto = new CompetitionHomePageDto();
                    competitionHomePageDto.setCompetitionDday(competition.getCompetitionDDay());
                    competitionHomePageDto.setCompetitionName(competition.getCompetitionName());
                    competitionHomePageDto.setCompetitionMatching(competition.getCompetitionMatching());
                    competitionHomePageDto.setCompetitionVisit(competition.getCompetitionVisit());
                    competitionHomePageDto.setCreatedAt(competition.getCreatedDate());
                    competitionHomePageDto.setCompetitionTypeList(competition.getCompetitionTypes());
                    return competitionHomePageDto;
                })
                .collect(Collectors.toList());

        CompetitionHomeTotalDto competitionHomeTotalDto = new CompetitionHomeTotalDto();
        competitionHomeTotalDto.setCompetitionList(competitionHomePageDtos);
        competitionHomeTotalDto.setPageNo(pageNo);
        competitionHomeTotalDto.setPageSize(pageSize);
        competitionHomeTotalDto.setTotalElements(competitionPage.getTotalElements());
        competitionHomeTotalDto.setTotalPages(competitionPage.getTotalPages());
        competitionHomeTotalDto.setLast(competitionPage.isLast());

        return ResponseUtils.ok(competitionHomeTotalDto, null);
    }
    @Transactional
    public ApiResponseDto<CompetitionSpecificDto> getSpecific (Long competitionId)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CompetitionSpecificDto competitionSpecificDto = competitionRepository.findById(competitionId)
                .map(competition -> CompetitionSpecificDto.builder()
                        .competitionId(competition.getCompetitionId())
                        .competitionName(competition.getCompetitionName())
                        .competitionUrl(competition.getCompetitionUrl())
                        .competitionHost(competition.getCompetitionHost())
                        .competitionPoster(competition.getCompetitionPoster())
                        .competitionAwards(competition.getCompetitionAwards())
                        .competitionStart(competition.getCompetitionStart().format(formatter))
                        .competitionEnd(competition.getCompetitionEnd().format(formatter))
                        .competitionContent(competition.getCompetitionContent())
                        .competitionVisit(competition.getCompetitionVisit()+1)
                        .competitionLike(competition.getCompetitionLike())
                        .competitionMatching(competition.getCompetitionMatching())
                        .competitionDDay(competition.getCompetitionDDay())
                        .competitionTypes(competition.getCompetitionTypes())
                        .build())
                .orElseThrow(() -> {
                    System.out.println("Competition not found");
                    return new RestApiException(ErrorType.NOT_FOUND_COMPETITION);
                });
        competitionRepository.updateVisit(competitionId);

        return ResponseUtils.ok(competitionSpecificDto, null);
    }
    // 최신순, 마감빠른순, 마감느린순, 인기순, 조회순, 팀빌딩순



}
