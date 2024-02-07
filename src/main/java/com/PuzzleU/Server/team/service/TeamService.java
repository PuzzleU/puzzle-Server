package com.PuzzleU.Server.team.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.competition.repository.CompetitionRepository;
import com.PuzzleU.Server.competition.dto.CompetitionSearchDto;
import com.PuzzleU.Server.competition.dto.CompetitionSearchTotalDto;
import com.PuzzleU.Server.friendship.dto.FriendShipSearchResponseDto;
import com.PuzzleU.Server.location.repository.LocationRepository;
import com.PuzzleU.Server.team.dto.TeamCreateDto;
import com.PuzzleU.Server.friendship.repository.FriendshipRepository;
import com.PuzzleU.Server.user.dto.UserSimpleDto;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;
    private final FriendshipRepository friendshipRepository;
    private final LocationRepository locationRepository;

    // 팀 구인글을 등록하는 API
    public ApiResponseDto<SuccessResponse> teamcreate(
            TeamCreateDto teamCreateDto
    )
    {
        // 공모전 검색해서 저장하고 - competition
        // 제목 넣고
        // 구인 포지션 중복 선택하고 - position
        // 모집 인우너수 정하고
        // 팀원 추가하고 - user
        // 대면 비대면 추가하고
        // 장소 추가하고 - location
        // 오픈 채팅 추가하고
        // 내용추가 하고 끝

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK,"팀구인글 생성완료"), null);
    }
    // 공모전 팀 등록시 공모전검색을 해서 값을 가져오는 API
    @Transactional
    public ApiResponseDto<CompetitionSearchTotalDto> competitionTeamSearch(int pageNo, int pageSize, String sortBy, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Competition> competitionPage;
        competitionPage = new PageImpl<>(competitionRepository.findByCompetitionName(keyword, pageable));
        List<CompetitionSearchDto> competitionSearchDtos = competitionPage.getContent().stream()
                .map(competition -> {
                    CompetitionSearchDto competitionSearchDto = new CompetitionSearchDto();
                    competitionSearchDto.setCompetitionId(competition.getCompetitionId());
                    competitionSearchDto.setCompetitionName(competition.getCompetitionName());
                    return competitionSearchDto;
                })
                .toList();
        CompetitionSearchTotalDto competitionSearchTotalDto = new CompetitionSearchTotalDto();
        competitionSearchTotalDto.setCompetitionSearchDtoList(competitionSearchDtos);
        competitionSearchTotalDto.setTotalPages(competitionPage.getTotalPages());
        competitionSearchTotalDto.setTotalElements(competitionPage.getTotalElements());
        competitionSearchTotalDto.setLast(competitionPage.isLast());
        competitionSearchTotalDto.setPageSize(pageSize);
        competitionSearchTotalDto.setPageNo(pageNo);

        return ResponseUtils.ok(competitionSearchTotalDto, null);
    }


    // 유저 정보 리스트를 가져오고 여기에는 유저의 이름, id, 한줄소개가 있어야한다.
    // 공모전글을 등록할때 내 친구들만 데이터를 가져오는 API
    @Transactional
    public ApiResponseDto<FriendShipSearchResponseDto> frIendRegister(String keyword, UserDetails loginUser,int pageNo, int pageSize, String sortBy)
    {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        Page<FriendShip> friendShips = friendshipRepository.findActiveFriendshipsForUserAndKeyword(user, keyword, pageable);
        List<FriendShip> friendShipList = friendShips.getContent();
        FriendShipSearchResponseDto friendShipSearchResponseDto = new FriendShipSearchResponseDto();
        // 각각의 friendshipe에 대해서 user가 아닌 user에 대한 정보를 저장하고 리스트로 만들어준다

        List<UserSimpleDto> userSimpleDtoList = new ArrayList<>();
        for(FriendShip friendShip:friendShipList)
        {
            User user1 = friendShip.getUser1();
            User user2 = friendShip.getUser2();
            if (user1 == user)
            {
                UserSimpleDto userSimpleDto = new UserSimpleDto();
                userSimpleDto.setUserProfile(user2.getUserProfile());
                userSimpleDto.setUserKoreaName(user2.getUserKoreaName());
                userSimpleDto.setUserRepresentativeProfileSentence(user2.getUserRepresentativeProfileSentence());
                userSimpleDto.setUserId(user2.getId());
                userSimpleDtoList.add(userSimpleDto);

            }
            else {
                UserSimpleDto userSimpleDto = new UserSimpleDto();
                userSimpleDto.setUserProfile(user1.getUserProfile());
                userSimpleDto.setUserKoreaName(user1.getUserKoreaName());
                userSimpleDto.setUserRepresentativeProfileSentence(user1.getUserRepresentativeProfileSentence());
                userSimpleDto.setUserId(user1.getId());
                userSimpleDtoList.add(userSimpleDto);

            }
        }
        friendShipSearchResponseDto.setUserSimpleDtoList(userSimpleDtoList);

     return ResponseUtils.ok(friendShipSearchResponseDto, null)  ;
    }
}
