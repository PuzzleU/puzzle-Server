package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.experience.ExperienceDto;
import com.PuzzleU.Server.dto.friendship.FriendShipSearchResponseDto;
import com.PuzzleU.Server.dto.relation.UserSkillsetRelationDto;
import com.PuzzleU.Server.dto.skillset.SkillSetDto;
import com.PuzzleU.Server.dto.skillset.SkillSetListDto;
import com.PuzzleU.Server.dto.university.UniversityDto;
import com.PuzzleU.Server.dto.user.*;
import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import com.PuzzleU.Server.service.User.UserService;
import com.PuzzleU.Server.service.experience.ExperienceService;
import com.PuzzleU.Server.service.skillset.SkillsetService;
import com.PuzzleU.Server.service.university.UniversityService;
import feign.Param;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final ExperienceService experienceService;
    private final SkillsetService skillsetService;
    private final UniversityService universityService;
    @PostMapping("/signup")
    public ApiResponseDto<SuccessResponse> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }
    // 기존에는 SuccessResponseDto를 통해서 userrequestsdto를 통하여 주입을 해주었다
    /*
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto> login(@RequestBody UserReqestsDto reqestsDto, BindingResult bindingResult)
    {
        return userService.signup(requestDto, bindingResult);
    }
    */

    @PostMapping("/login")
    public ApiResponseDto<SuccessResponse> login(@RequestBody LoginRequestsDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }

    @PatchMapping("/optional")
    public ApiResponseDto<SuccessResponse> registerOptional(@Valid
            @RequestBody UserRegisterOptionalDto userRegisterOptionalDto,
            @AuthenticationPrincipal UserDetails loginUser)
    {
        return userService.createRegisterOptionalUser(loginUser,userRegisterOptionalDto);
    }
    @PostMapping("/experience")
    public ApiResponseDto<SuccessResponse> postExperience(@Valid
            @RequestBody ExperienceDto experienceDto, @AuthenticationPrincipal UserDetails loginUser)
    {
        return experienceService.createExperience(loginUser,experienceDto);
    }
    @PatchMapping("/experience/{experienceId}")
    public ApiResponseDto<SuccessResponse> updateExperience(@Valid
            @RequestBody ExperienceDto experienceDto, @AuthenticationPrincipal UserDetails loginUser, @PathVariable Long experienceId)
    {
        return experienceService.updateExperience(loginUser, experienceId,experienceDto);
    }
    @DeleteMapping("/experience/{experienceId}")
    public ApiResponseDto<SuccessResponse> deleteExperience(@Valid
            @AuthenticationPrincipal UserDetails loginUser
            , @PathVariable Long experienceId)
    {
        return experienceService.deleteExperience(loginUser, experienceId);
    }
    @GetMapping("/experience")
    public ApiResponseDto<List<ExperienceDto>> getExperienceList(@Valid
           @AuthenticationPrincipal UserDetails loginUser
    )
    {
        return experienceService.getExperienceList(loginUser);
    }
    @GetMapping("/experience/{experienceId}")
    public ApiResponseDto<ExperienceDto> getExperience(@Valid
          @AuthenticationPrincipal UserDetails loginUser, @PathVariable Long experienceId
    )
    {
        return experienceService.getExperience(loginUser,experienceId);
    }
    @PostMapping("/skillset")
    public ApiResponseDto<SuccessResponse> createSkillset(@Valid
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestBody SkillSetListDto skillsetList
    )
    {
        return skillsetService.createSkillset(loginUser, skillsetList);
    }
    @DeleteMapping("/skillset/{skillsetId}")
    public ApiResponseDto<SuccessResponse> deleteSkillset(@Valid
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long skillsetId
    )
    {
        return skillsetService.deleteSkillset(loginUser, skillsetId);
    }
    @GetMapping("/skillset")
    public ApiResponseDto<List<UserSkillsetRelationDto>> getUserSkillsetList(@Valid
            @AuthenticationPrincipal UserDetails loginUser

    )
    {
        return skillsetService.getSkillsetList(loginUser);
    }
    @PatchMapping("/university")
    public ApiResponseDto<SuccessResponse> createUniversity(@Valid
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestBody UserUniversityDto userUniversityDto
            )
    {
        return universityService.createUniversity(loginUser, userUniversityDto);
    }

    @PatchMapping("/essential")
    public ApiResponseDto<SuccessResponse> registerEssential(
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestBody UserRegisterEssentialDto userRegisterEssentialDto
    ) {
        return userService.registerEssential(loginUser, userRegisterEssentialDto);
    }
    @GetMapping("/friendSearch")
    public ApiResponseDto<FriendShipSearchResponseDto> searchUser(
            @Valid
            @RequestParam(value = "search", defaultValue = "None", required = false) String search,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy
    )
    {
        return userService.searchUser(pageNo, pageSize, sortBy, search);
    }

//2
}

