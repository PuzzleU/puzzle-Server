package com.PuzzleU.Server.user.controller;

import com.PuzzleU.Server.apply.service.ApplyService;
import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.experience.dto.ExperienceDto;
import com.PuzzleU.Server.friendship.dto.FriendShipSearchResponseDto;
import com.PuzzleU.Server.friendship.service.FriendshipService;
import com.PuzzleU.Server.relations.dto.UserSkillsetRelationDto;
import com.PuzzleU.Server.relations.dto.UserSkillsetRelationListDto;
import com.PuzzleU.Server.skillset.dto.SkillSetListDto;
import com.PuzzleU.Server.experience.service.ExperienceService;
import com.PuzzleU.Server.skillset.service.SkillsetService;
import com.PuzzleU.Server.team.dto.*;
import com.PuzzleU.Server.team.service.TeamService;
import com.PuzzleU.Server.university.service.UniversityService;
import com.PuzzleU.Server.user.dto.*;
import com.PuzzleU.Server.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final ApplyService applyService;
    private final TeamService teamService;
    private final FriendshipService friendshipService;

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
    @DeleteMapping("/skillset/{userSkillsetId}")
    public ApiResponseDto<SuccessResponse> deleteSkillset(@Valid
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long userSkillsetId
    )
    {
        return skillsetService.deleteSkillset(loginUser, userSkillsetId);
    }
    @GetMapping("/skillset")
    public ApiResponseDto<List<UserSkillsetRelationListDto>> getUserSkillsetList(@Valid
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

    @PatchMapping("/profile")
    public ApiResponseDto<SuccessResponse> updateUserProfileBasic(
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestBody UserProfileBasicDto userProfileBasicDto
    ) {
        return userService.updateUserProfileBasic(loginUser, userProfileBasicDto);
    }
    @PostMapping("/friend/{friendId}")
    public ApiResponseDto<SuccessResponse> registerFriend(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long friendId)
    {
        return friendshipService.registerFriend(loginUser, friendId);
    }
    @PatchMapping("/friend/{friendId}")
    public ApiResponseDto<SuccessResponse> responseFriend(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long friendId)
    {
        return friendshipService.responseFriend(loginUser, friendId);
    }
    @DeleteMapping("/friend/{friendId}")
    public ApiResponseDto<SuccessResponse> deleteFriend(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long friendId)
    {
        return friendshipService.deleteFriend(loginUser, friendId);
    }
    @GetMapping("/apply")
    public ApiResponseDto<ApplyTeamDto> getApply(
            @Valid
            @AuthenticationPrincipal UserDetails loginUser
    )
    {
        return applyService.getApply(loginUser);
    }
    @GetMapping("/apply/{type}")
    public ApiResponseDto<ApplyTeamListDto> getApplyType(
            @Valid
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "applyid", required = false) String sortBy,
            @PathVariable(value = "type",required = false) String type
    )
    {
        return applyService.getApplyType(loginUser,pageNo,pageSize,sortBy,type);
    }
    @DeleteMapping("/apply/{apply_id}")
    public ApiResponseDto<SuccessResponse> deleteApply(
            @Valid
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long apply_id
    )
    {
        return applyService.deleteApply(loginUser, apply_id);
    }
    @GetMapping("/team")
    public ApiResponseDto<TeamApplyDto> teamApplyTotal(
            @AuthenticationPrincipal UserDetails loginUser
    )
    {
        return teamService.getTeamApplyTotal(loginUser);
    }
    @GetMapping("/team/{type}")
    public ApiResponseDto<TeamListDto> teamApplyType(
            @Valid
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "team_user_id", required = false) String sortBy,
            @PathVariable(value = "type",required = false) String type
    )
    {
        return teamService.getTeamApplyType(loginUser,pageNo,pageSize,sortBy,type);
    }
    @PatchMapping("/team/{team_id}")
    public ApiResponseDto<SuccessResponse> teamStatus(
            @AuthenticationPrincipal UserDetails loginUser,
            @PathVariable Long team_id,
            @RequestBody TeamStatusDto teamStatusDto

            )
    {
        return teamService.teamStatus(loginUser, team_id, teamStatusDto);
    }

    @GetMapping("/profile/my")
    public ApiResponseDto<UserMyProfileDto> readMyProfile(@AuthenticationPrincipal UserDetails loginUser) {
        return userService.readMyProfile(loginUser);
    }



}

