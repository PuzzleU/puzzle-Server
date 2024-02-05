package com.PuzzleU.Server.service.team;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.competition.CompetitionSearchDto;
import com.PuzzleU.Server.dto.team.TeamCreateDto;
import com.PuzzleU.Server.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
