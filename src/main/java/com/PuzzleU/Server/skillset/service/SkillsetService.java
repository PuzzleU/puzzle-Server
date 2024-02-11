package com.PuzzleU.Server.skillset.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.relations.dto.UserSkillsetRelationDto;
import com.PuzzleU.Server.relations.dto.UserSkillsetRelationListDto;
import com.PuzzleU.Server.skillset.entity.Skillset;
import com.PuzzleU.Server.skillset.repository.SkillsetRepository;
import com.PuzzleU.Server.skillset.dto.SkillSetDto;
import com.PuzzleU.Server.skillset.dto.SkillSetListDto;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.relations.entity.UserSkillsetRelation;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.user.repository.UserRepository;
import com.PuzzleU.Server.relations.repository.UserSkillsetRelationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillsetService {

    private final UserRepository userRepository;
    private final SkillsetRepository skillsetRepository;
    private final UserSkillsetRelationRepository userSkillsetRelationRepository;

    // 유저가 본인의 스킬셋을 등록가능한 API
    public ApiResponseDto<SuccessResponse> createSkillset(
            UserDetails loginUser, SkillSetListDto skillsetList
    )
    {
        // skillset id 로 skillset을 찾고 userid로 user를 찾고 넣어주면 된다. 중복가능
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        // 리스트형식인 skillsetlistdto 에서 for문 돌려서 하나씩 dto오를 참여해서 각각 s, u , l 을 설정한 것을 하나씩 repository에 저장한다
        List<UserSkillsetRelation> userSkillsetRelationList = new ArrayList<>();
        for(SkillSetDto skillsetDto : skillsetList.getSkillSetDtoList())
        {
            Optional<Skillset> optionalSkillset = skillsetRepository.findById(skillsetDto.getSkillSetId());
            System.out.println(skillsetDto.getSkillSetId());
            Skillset skillset = optionalSkillset.orElseThrow(() -> {
                System.out.println("Skillset not found");
                return new RestApiException(ErrorType.NOT_FOUND_SKILLSET);
            });
            UserSkillsetRelation userSkillsetRelation = new UserSkillsetRelation();
            userSkillsetRelation.setLevel(skillsetDto.getSkillSetLevel());
            userSkillsetRelation.setUser(user);
            userSkillsetRelation.setSkillset(skillset);
            userSkillsetRelationList.add(userSkillsetRelation);
        }
        userSkillsetRelationRepository.saveAll(userSkillsetRelationList);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "유저스킬셋 저장완료"), null);

    }
    // 유저가 자신의 스킬셋을 삭제가능함
    public ApiResponseDto<SuccessResponse> deleteSkillset(
            UserDetails loginUser, Long skillsetId
    )
    {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Skillset> userSkillset = skillsetRepository.findById(skillsetId);
        Skillset skillset = userSkillset.orElseThrow(() -> {
            System.out.println("Skillset not found");
            return new RestApiException(ErrorType.NOT_FOUND_SKILLSET);
        });
        Optional<UserSkillsetRelation> userSkillsetRelation = userSkillsetRelationRepository.findByUserAndSkillset(user,skillset);
        UserSkillsetRelation userSkillsetrelation = userSkillsetRelation.orElseThrow(() -> {
            System.out.println("UserSkillset not found");
            return new RestApiException(ErrorType.NOT_FOUND_USERSKILLSETRELATION);
        });
        userSkillsetRelationRepository.delete(userSkillsetrelation);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "유저스킬셋 삭제완료"), null);

    }
    // 유저가 자신이 등록한 스킬셋의 리스트를 확인가능한 API
    public ApiResponseDto<List<UserSkillsetRelationListDto>> getSkillsetList(UserDetails loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));


        List<UserSkillsetRelation> userSkillsetRelation = userSkillsetRelationRepository.findByUser(user);
        List<UserSkillsetRelationListDto> userSkillsetRelationListDtos = new ArrayList<>();
        for (UserSkillsetRelation userSkillsetrelation : userSkillsetRelation)
        {
            UserSkillsetRelationListDto userSkillsetRelationListDto = new UserSkillsetRelationListDto();
            userSkillsetRelationListDto.setUserSkillsetRelationId(userSkillsetrelation.getUserSkillsetRelationId());
            userSkillsetRelationListDto.setSkillsetId(userSkillsetrelation.getSkillset().getSkillsetId());
            userSkillsetRelationListDto.setLevel(userSkillsetrelation.getLevel());
            userSkillsetRelationListDtos.add(userSkillsetRelationListDto);
        }

        return ResponseUtils.ok(userSkillsetRelationListDtos, null);

    }
}
