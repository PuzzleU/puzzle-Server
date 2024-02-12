package com.PuzzleU.Server.apply.service;

import com.PuzzleU.Server.apply.dto.ApplyDetailDto;
import com.PuzzleU.Server.apply.dto.ApplyPostDto;
import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.apply.repository.ApplyRepository;
import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.ApplyStatus;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.position.dto.PositionDto;
import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.position.repository.PositionRepository;
import com.PuzzleU.Server.relations.entity.PositionApplyRelation;
import com.PuzzleU.Server.relations.repository.PositionApplyRelationRepository;
import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.team.repository.TeamRepository;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApplyService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final PositionApplyRelationRepository positionApplyRelationRepository;
    private final PositionRepository positionRepository;
    private final ApplyRepository applyRepository;

    // 팀에 대한 지원서 작성
    public ApiResponseDto<SuccessResponse> postApply(UserDetails loginUser, Long teamId, ApplyPostDto applyPostDto) {
        Team team = teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_TEAM));
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        Apply apply = Apply.builder()
                .applyTitle(applyPostDto.getApplyTitle())
                .applyContent(applyPostDto.getApplyContent())
                .applyStatus(ApplyStatus.WAITING)
                .team(team)
                .user(user)
                .build();

        applyRepository.save(apply);

        for (Long positionId : applyPostDto.getApplyPositionIdList()) {
            Position position = positionRepository.findByPositionId(positionId)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_POSITION));

            PositionApplyRelation positionApplyRelation = PositionApplyRelation.builder()
                    .apply(apply)
                    .position(position).build();

            positionApplyRelationRepository.save(positionApplyRelation);
        }

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "지원서 저장 완료"), null);
    }

    // 지원서 상세
    public ApiResponseDto<ApplyDetailDto> applyDetail(Long applyId) {

        Apply apply = applyRepository.findByApplyId(applyId)
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_APPLY));

        Team team = apply.getTeam();

        Competition competition  = team.getCompetition();

        List<PositionApplyRelation> positionApplyRelationList = positionApplyRelationRepository.findByApply(apply);

        System.out.println(positionApplyRelationList);

        List<PositionDto> positionDtoList = new ArrayList<>();
        for (PositionApplyRelation rel : positionApplyRelationList) {
            PositionDto positionDto = PositionDto.builder()
                    .PositionId(rel.getPosition().getPositionId())
                    .PositionName(rel.getPosition().getPositionName()).build();
            positionDtoList.add(positionDto);
        }


        ApplyDetailDto applyDetailDto = new ApplyDetailDto();
        applyDetailDto.setCompetitionPoster(competition.getCompetitionPoster());
        applyDetailDto.setCompetitionTitle(competition.getCompetitionName());
        applyDetailDto.setTeamTitle(team.getTeamTitle());
        applyDetailDto.setPositionList(positionDtoList);
        applyDetailDto.setApplyTitle(apply.getApplyTitle());
        applyDetailDto.setApplyContent(apply.getApplyContent());

        return ResponseUtils.ok(applyDetailDto, null);
    }


}
