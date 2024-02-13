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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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
    private final Logger logger = LoggerFactory.getLogger(CompetitionService.class);

    // 공모전 전체를 볼 수 있는 데이터를 넘기는 API
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
        if (competitionPage == null) {
            throw new RestApiException(ErrorType.NOT_FOUND);
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
    @Transactional
    public ApiResponseDto<TeamListDto> getTeamList(Long competition_Id, int pageNo, int pageSize, String sortBy) {
        try {
            TeamListDto teamListDto = new TeamListDto();
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

            // 경기 정보 찾기
            Competition competition = competitionRepository.findById(competition_Id)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_COMPETITION));

            // 해당 경기에 속한 팀들 가져오기
            Page<Team> teamPage;
            teamPage = new PageImpl<>(teamRepository.findByCompetition(competition, pageable));
            // 팀 목록 및 팀 정보 DTO 작성
            List<TeamAbstractBaseDto> teamAbstractDtos = teamPage.getContent().stream()
                    .map(team -> {
                        TeamAbstractBaseDto teamAbstractDto = new TeamAbstractBaseDto();
                        // 팀 작성자 설정
                        List<TeamUserRelation> teamUserRelation = teamUserRepository.findByTeam(team);
                        for (TeamUserRelation teamuserRelation : teamUserRelation) {
                            if (teamuserRelation.getIsWriter()) {
                                teamAbstractDto.setTeamWriter(teamuserRelation.getUser().getUserKoreaName());
                                break;
                            }
                        }
                        // 팀 위치 설정
                        List<TeamLocationRelation> teamLocationRelation = teamLocationRelationRepository.findByTeam(team);
                        List<String> locationList = new ArrayList<>();
                        for (TeamLocationRelation teamLocationRelation1 : teamLocationRelation) {
                            String location = teamLocationRelation1.getLocation().getLocationName();
                            locationList.add(location);
                        }
                        teamAbstractDto.setTeamId(team.getTeamId());
                        teamAbstractDto.setTeamNeed(team.getTeamMemberNeed());
                        teamAbstractDto.setTeamNowMember(team.getTeamMemberNow());
                        teamAbstractDto.setTeamTitle(team.getTeamTitle());
                        teamAbstractDto.setTeamPoster(competition.getCompetitionPoster());
                        teamAbstractDto.setTeamLocations(locationList);
                        // 포지션 설정
                        List<String> positionList = team.getPositionList().stream()
                                .map(Position::getPositionName)
                                .collect(Collectors.toList());
                        teamAbstractDto.setPositionList(positionList);
                        return teamAbstractDto;
                    })
                    .collect(Collectors.toList());

            // 팀 목록 DTO 설정
            teamListDto.setTotalTeam((int) teamPage.getTotalElements());
            teamListDto.setTotalElements(teamPage.getTotalElements());
            teamListDto.setTeamList(teamAbstractDtos);
            teamListDto.setLast(teamPage.isLast());
            teamListDto.setTotalPages(teamPage.getTotalPages());
            teamListDto.setPageNo(pageNo);
            teamListDto.setPageSize(pageSize);

            return ResponseUtils.ok(teamListDto, null);
        } catch (RestApiException e) {
            // 이미 정의된 예외인 경우 그대로 던집니다.
            throw e;
        } catch (NoSuchElementException e) {
            // 경기 또는 팀이 존재하지 않는 경우 예외 처리합니다.
            throw new RestApiException(ErrorType.NOT_FOUND);
        } catch (Exception e) {
            // 서버 오류로 처리합니다.
            throw new RestApiException(ErrorType.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ApiResponseDto<TeamSpecificDto> getTeamSpecific(Long competition_Id, Long team_Id) {
        try {
            TeamSpecificDto teamSpecificDto = new TeamSpecificDto();
            Competition competition = competitionRepository.findById(competition_Id)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_COMPETITION));

            Team team = teamRepository.findById(team_Id)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_TEAM));

            // 팀 작성자 설정

            List<TeamUserRelation> writerList = teamUserRepository.findByTeam(team);
            if (!writerList.isEmpty()) {
                for(TeamUserRelation teamUserRelation : writerList) {
                    if(teamUserRelation.getIsWriter()) {
                        teamSpecificDto.setTeamWriter(teamUserRelation.getUser().getUserKoreaName());
                        break;
                    }
                }
            } else {
                throw new RestApiException(ErrorType.NOT_FOUND);
            }



            // 팀 위치 설정
            List<TeamLocationRelation> teamLocationRelation = teamLocationRelationRepository.findByTeam(team);
            List<String> locationList = new ArrayList<>();
            for (TeamLocationRelation teamLocationRelation1 : teamLocationRelation) {
                String location = teamLocationRelation1.getLocation().getLocationName();
                locationList.add(location);
            }
            teamSpecificDto.setTeamNeed(team.getTeamMemberNeed());
            teamSpecificDto.setTeamNowMember(team.getTeamMemberNow());
            teamSpecificDto.setTeamTitle(team.getTeamTitle());
            teamSpecificDto.setTeamPoster(competition.getCompetitionPoster());
            teamSpecificDto.setTeamLocations(locationList);

            // 포지션 설정
            List<String> PositionList = team.getPositionList().stream()
                    .map(Position::getPositionName)
                    .collect(Collectors.toList());
            teamSpecificDto.setPositionList(PositionList);

            teamSpecificDto.setTeamContent(team.getTeamContent());
            teamSpecificDto.setTeamIntroduce(team.getTeamIntroduce());

            // 팀 멤버 설정
            List<UserSimpleDto> userSimpleDtoList = new ArrayList<>();
            List<TeamUserRelation> teamUserRelations = teamUserRepository.findByTeam(team);
            for (TeamUserRelation teamUserRelation1 : teamUserRelations) {
                UserSimpleDto userSimpleDto = new UserSimpleDto();
                userSimpleDto.setUserId(teamUserRelation1.getUser().getId());
                userSimpleDto.setUserProfile(teamUserRelation1.getUser().getUserProfile());
                userSimpleDto.setUserKoreaName(teamUserRelation1.getUser().getUserKoreaName());
                userSimpleDto.setUserRepresentativeProfileSentence(teamUserRelation1.getUser().getUserRepresentativeProfileSentence());
                userSimpleDtoList.add(userSimpleDto);
            }
            teamSpecificDto.setMembers(userSimpleDtoList);

            return ResponseUtils.ok(teamSpecificDto, null);
        } catch (RestApiException e) {
            throw e; // 이미 정의된 예외인 경우 그대로 던집니다.
        } catch (Exception e) {
            throw new RestApiException(ErrorType.INTERNAL_SERVER_ERROR);
        }
    }

    // 팀을 검색했을때 정보 얻기
    @Transactional
    public ApiResponseDto<TeamListDto> getTeamSearchList(int pageNo, int pageSize, String sortBy, String search)


    {
        TeamListDto teamListDto = new TeamListDto();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Team> teamPage;

        teamPage = new PageImpl<>(teamRepository.findByTeamTitleContaining(search, pageable));
        List<TeamAbstractBaseDto> teamAbstractDtos;
        teamAbstractDtos = teamPage.getContent().stream()
                .map(team ->
                {
                    TeamAbstractBaseDto teamAbstractDto = new TeamAbstractBaseDto();
                    List<TeamUserRelation> teamUserRelation = teamUserRepository.findByTeam(team);
                    for (TeamUserRelation teamuserRelation : teamUserRelation) {
                        if (teamuserRelation.getIsWriter())
                        {
                            System.out.println(teamuserRelation.getIsWriter());
                            System.out.println(teamuserRelation.getUser().getUserKoreaName());
                            teamAbstractDto.setTeamWriter(teamuserRelation.getUser().getUserKoreaName());
                            break;
                        }
                    }
                    List<TeamLocationRelation> teamLocationRelation = teamLocationRelationRepository.findByTeam(team);
                    List<String> locationList = new ArrayList<>();
                    for (TeamLocationRelation teamLocationRelation1 : teamLocationRelation) {
                        String location = teamLocationRelation1.getLocation().getLocationName();
                        locationList.add(location);
                    }
                    teamAbstractDto.setTeamId(team.getTeamId());
                    teamAbstractDto.setTeamNeed(team.getTeamMemberNeed());
                    teamAbstractDto.setTeamNowMember(team.getTeamMemberNow());
                    teamAbstractDto.setTeamTitle(team.getTeamTitle());
                    teamAbstractDto.setTeamPoster(team.getCompetition().getCompetitionPoster());
                    teamAbstractDto.setTeamLocations(locationList);
                    List<String> PositionList = new ArrayList<>();
                    for(Position position : team.getPositionList())
                    {
                        PositionList.add(position.getPositionName());
                    }
                    teamAbstractDto.setPositionList(PositionList);
                    return teamAbstractDto;
                })
                .collect(Collectors.toList());
        teamListDto.setTeamList(teamAbstractDtos);
        teamListDto.setLast(teamPage.isLast());
        teamListDto.setTotalPages(teamPage.getTotalPages());
        teamListDto.setTotalElements(teamPage.getTotalElements());
        teamListDto.setPageNo(pageNo);
        teamListDto.setPageSize(pageSize);

        return ResponseUtils.ok(teamListDto, null);
    }
    @Transactional
    public ApiResponseDto<SuccessResponse> heartCreate(UserDetails loginuser, Long competitionId)
    {
        User user = userRepository.findByUsername(loginuser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        Competition competition = competitionOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND_COMPETITION)
        );
        List<Heart> hearts = heartRepository.findByCompetitionAndUser(competition,user);
        List<Heart> user_heart = heartRepository.findByCompetitionAndUserNot(competition,user);
        int heart = hearts.size();
        if (user_heart.size()==0)
        {
            heart +=1;
            Heart heart_user = new Heart();
            heart_user.setCompetition(competition);
            heart_user.setUser(user);
            heartRepository.save(heart_user);
        }
        else
        {
            throw new RestApiException(ErrorType.FOUND_LIKE);
        }
        competition.setCompetitionLike(heart);
        competitionRepository.save(competition);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "좋아요를 눌렀습니다"), null);
    }
    @Transactional
    public ApiResponseDto<SuccessResponse> heartDelete(UserDetails loginuser, Long competitionId)
    {
        User user = userRepository.findByUsername(loginuser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        Competition competition = competitionOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND_COMPETITION)
        );
        List<Heart> hearts = heartRepository.findByCompetitionAndUser(competition,user);
        Heart user_heart = heartRepository.findOneByCompetitionAndUserNot(competition,user);
        int heart = hearts.size();
        heart -=1;
        heartRepository.delete(user_heart);
        competition.setCompetitionLike(heart);
        competitionRepository.save(competition);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "좋아요를 취소하였습니다"), null);
    }
}
