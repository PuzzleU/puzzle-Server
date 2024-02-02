package com.PuzzleU.Server.controller;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.experience.ExperienceDto;
import com.PuzzleU.Server.dto.relation.UserSkillsetRelationDto;
import com.PuzzleU.Server.dto.skillset.SkillSetDto;
import com.PuzzleU.Server.dto.skillset.SkillSetListDto;
import com.PuzzleU.Server.dto.user.LoginRequestsDto;
import com.PuzzleU.Server.dto.user.SignupRequestDto;
import com.PuzzleU.Server.dto.user.UserRegisterOptionalDto;
import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import com.PuzzleU.Server.service.User.UserService;
import com.PuzzleU.Server.service.experience.ExperienceService;
import com.PuzzleU.Server.service.skillset.SkillsetService;
import com.PuzzleU.Server.service.university.UniversityService;
import feign.Param;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping("/optional/{userId}")
    public ApiResponseDto<SuccessResponse> registerOptional(
            @RequestBody UserRegisterOptionalDto userRegisterOptionalDto,
            @PathVariable Long userId)
    {
        return userService.createRegisterOptionalUser(userId,userRegisterOptionalDto);
    }
    @PostMapping("/experience/{userId}")
    public ApiResponseDto<SuccessResponse> postExperience(
            @RequestBody ExperienceDto experienceDto,
            @PathVariable Long userId)
    {
        return experienceService.createExperience(userId,experienceDto);
    }
    @PatchMapping("/experience/{userId}/{experienceId}")
    public ApiResponseDto<SuccessResponse> updateExperience(
            @RequestBody ExperienceDto experienceDto,
            @PathVariable Long userId, @PathVariable Long experienceId)
    {
        return experienceService.updateExperience(userId, experienceId,experienceDto);
    }
    @DeleteMapping("/experience/{userId}/{experienceId}")
    public ApiResponseDto<SuccessResponse> deleteExperience(
            @PathVariable Long userId, @PathVariable Long experienceId)
    {
        return experienceService.deleteExperience(userId, experienceId);
    }
    @GetMapping("/experience/{userId}")
    public ApiResponseDto<List<ExperienceDto>> getExperienceList(
            @PathVariable Long userId
    )
    {
        return experienceService.getExperienceList(userId);
    }
    @GetMapping("/experience/{userId}/{experienceId}")
    public ApiResponseDto<ExperienceDto> getExperience(
            @PathVariable Long userId, @PathVariable Long experienceId
    )
    {
        return experienceService.getExperience(userId,experienceId);
    }
    @PostMapping("/skillset/{userId}")
    public ApiResponseDto<SuccessResponse> createSkillset(
            @PathVariable Long userId,
            @RequestBody SkillSetListDto skillsetList
    )
    {
        return skillsetService.createSkillset(userId, skillsetList);
    }
    @DeleteMapping("/skillset/{userId}/{skillsetId}")
    public ApiResponseDto<SuccessResponse> deleteSkillset(
            @PathVariable Long userId,
            @PathVariable Long skillsetId
    )
    {
        return skillsetService.deleteSkillset(userId, skillsetId);
    }
    @GetMapping("/skillset/{userId}")
    public ApiResponseDto<List<UserSkillsetRelationDto>> getUserSkillsetList(
            @PathVariable Long userId
    )
    {
        return skillsetService.getSkillsetList(userId);
    }
    @PostMapping("/university/{userId}")
    public ApiResponseDto<SuccessResponse> createUniversity(
            @PathVariable Long userId
    )
    {
        return universityService.createUniversity(userId);
    }
}

