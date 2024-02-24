package com.PuzzleU.Server.competition.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.CompetitionType;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.competition.dto.CompetitionHomePageDto;
import com.PuzzleU.Server.competition.dto.CompetitionHomeTotalDto;
import com.PuzzleU.Server.competition.dto.CompetitionSpecificDto;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.competition.repository.CompetitionRepository;
import com.PuzzleU.Server.heart.entity.Heart;
import com.PuzzleU.Server.heart.repository.HeartRepository;
import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.relations.entity.TeamLocationRelation;
import com.PuzzleU.Server.relations.entity.TeamUserRelation;
import com.PuzzleU.Server.relations.repository.TeamLocationRelationRepository;
import com.PuzzleU.Server.relations.repository.TeamPositionRelationRepository;
import com.PuzzleU.Server.relations.repository.TeamUserRepository;
import com.PuzzleU.Server.team.dto.TeamAbstractBaseDto;
import com.PuzzleU.Server.team.dto.TeamAbstractDto;
import com.PuzzleU.Server.team.dto.TeamListDto;
import com.PuzzleU.Server.team.dto.TeamSpecificDto;
import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.team.repository.TeamRepository;
import com.PuzzleU.Server.user.dto.UserSimpleDto;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;
    private final TeamLocationRelationRepository teamLocationRelationRepository;
    private final TeamUserRepository teamUserRepository;
    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final TeamPositionRelationRepository teamPositionRelationRepository;
    private final Logger logger = LoggerFactory.getLogger(CompetitionService.class);

    // 공모전 전체를 볼 수 있는 데이터를 넘기는 API
    @Transactional
    public ApiResponseDto<CompetitionHomeTotalDto> getHomepage(int pageNo, int pageSize, String sortBy, String keyword, CompetitionType type) {
        Pageable pageable;
        if (sortBy.equals("competitionEndReverse"))
        {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("competitionEnd").ascending());
        }
        else{
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        }
        Page<Competition> competitionPage;
        String None = "None";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
        if (competitionPage == null) {
            throw new RestApiException(ErrorType.NOT_FOUND);
        }
        //LocalDate now = LocalDate.now();

        List<CompetitionHomePageDto> competitionHomePageDtos = competitionPage.getContent().stream()
                .map(competition -> {

                    Integer heartNumber = (heartRepository.findByCompetition(competition.getCompetitionId())).size();

                    CompetitionHomePageDto competitionHomePageDto = new CompetitionHomePageDto();
                    competitionHomePageDto.setCompetitionHeart(heartNumber);
                    competitionHomePageDto.setCompetitionPoster(competition.getCompetitionPoster());
                    competitionHomePageDto.setCompetitionId(competition.getCompetitionId());
                    competitionHomePageDto.setCompetitionDday(competition.getCompetitionDDay());
                    competitionHomePageDto.setCompetitionName(competition.getCompetitionName());
                    competitionHomePageDto.setCompetitionMatching(competition.getCompetitionMatching());
                    competitionHomePageDto.setCompetitionVisit(competition.getCompetitionVisit());
                    competitionHomePageDto.setCreatedAt(competition.getCreatedDate().format(formatter));
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

    // 공모전 하나의 정보를 전체다 볼 수 있는 API
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

    // 공모전 팀 구인글 리스트


    //a
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateCompetitionDday() {
        List<Competition> competitions = competitionRepository.findAll();
        LocalDateTime today = LocalDateTime.now();
        for (Competition competition : competitions) {
            LocalDateTime competitionEnd = competition.getCompetitionEnd();
            if (competitionEnd != null) {
                long daysUntilEnd = ChronoUnit.DAYS.between(today, competitionEnd);
                competition.setCompetitionDDay((int) daysUntilEnd);
                competitionRepository.save(competition);
            }
        }
    }

}
