package com.PuzzleU.Server.service.skillset;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.experience.ExperienceDto;
import com.PuzzleU.Server.dto.relation.UserSkillsetRelationDto;
import com.PuzzleU.Server.dto.skillset.SkillSetDto;
import com.PuzzleU.Server.dto.skillset.SkillSetListDto;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.entity.experience.Experience;
import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import com.PuzzleU.Server.entity.skillset.Skillset;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.repository.SkillsetRepository;
import com.PuzzleU.Server.repository.UserRepository;
import com.PuzzleU.Server.repository.UserSkillsetRelationRepository;
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
    public ApiResponseDto<List<UserSkillsetRelationDto>> getSkillsetList(UserDetails loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));


        List<UserSkillsetRelation> userSkillsetRelation = userSkillsetRelationRepository.findByUser(user);
        List<UserSkillsetRelationDto> userSkillsetRelationDtos = new ArrayList<>();
        for (UserSkillsetRelation userSkillsetrelation : userSkillsetRelation)
        {
            UserSkillsetRelationDto userSkillsetRelationDto = new UserSkillsetRelationDto();
            userSkillsetRelationDto.setUserId(user.getId());
            userSkillsetRelationDto.setSkillsetId(userSkillsetrelation.getSkillset().getSkillsetId());
            userSkillsetRelationDto.setLevel(userSkillsetrelation.getLevel());
            userSkillsetRelationDtos.add(userSkillsetRelationDto);
        }

        return ResponseUtils.ok(userSkillsetRelationDtos, null);

    }
}
