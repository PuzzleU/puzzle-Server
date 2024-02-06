package com.PuzzleU.Server.service.team;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.competition.CompetitionSearchDto;
import com.PuzzleU.Server.dto.friendship.FriendShipSearchResponseDto;
import com.PuzzleU.Server.dto.team.TeamCreateDto;
import com.PuzzleU.Server.dto.user.UserSimpleDto;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.entity.friendship.FriendShip;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;
    private final FriendshipRepository friendshipRepository;
    private final LocationRepository locationRepository;

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
    @Transactional
    public ApiResponseDto<List<CompetitionSearchDto>> competitionTeamSearch(String keyword) {
        List<CompetitionSearchDto> competitionSearchDtos = competitionRepository.findByCompetitionName(keyword).stream()
                .map(competition -> CompetitionSearchDto.builder()
                        .competitionName(competition.getCompetitionName())
                        .competitionId(competition.getCompetitionId())
                        // 다른 필드들도 필요에 따라 추가
                        .build())
                .collect(Collectors.toList());

        return ResponseUtils.ok(competitionSearchDtos, null);
    }
    // 유저 정보 리스트를 가져오고 여기에는 유저의 이름, id, 한줄소개가 있어야한다.
    @Transactional
    public ApiResponseDto<FriendShipSearchResponseDto> firendRegister(String keyword, UserDetails loginUser)
    {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        FriendShipSearchResponseDto friendShipSearchResponseDto = new FriendShipSearchResponseDto();
        // 각각의 friendshipe에 대해서 user가 아닌 user에 대한 정보를 저장하고 리스트로 만들어준다
        List<FriendShip> friendShipList = friendshipRepository.findActiveFriendshipsForUserAndKeyword(user, keyword);
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
