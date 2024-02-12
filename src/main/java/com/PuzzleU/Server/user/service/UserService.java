package com.PuzzleU.Server.user.service;

import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.apply.repository.ApplyRepository;
import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ErrorResponse;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.Level;
import com.PuzzleU.Server.common.enumSet.WorkType;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.experience.dto.ExperienceDto;
import com.PuzzleU.Server.experience.repository.ExperienceRepository;
import com.PuzzleU.Server.friendship.dto.FriendShipSearchResponseDto;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.friendship.repository.FriendshipRepository;
import com.PuzzleU.Server.position.dto.PositionDto;
import com.PuzzleU.Server.position.repository.PositionRepository;
import com.PuzzleU.Server.profile.dto.ProfileDto;
import com.PuzzleU.Server.profile.repository.ProfileRepository;
import com.PuzzleU.Server.relations.entity.*;
import com.PuzzleU.Server.relations.repository.*;
import com.PuzzleU.Server.skillset.dto.SkillSetDto;
import com.PuzzleU.Server.experience.entity.Experience;
import com.PuzzleU.Server.interest.entity.Interest;
import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.major.repository.MajorRepository;
import com.PuzzleU.Server.major.entity.Major;
import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.profile.entity.Profile;
import com.PuzzleU.Server.skillset.entity.Skillset;
import com.PuzzleU.Server.skillset.repository.SkillsetRepository;
import com.PuzzleU.Server.team.dto.*;
import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.team.repository.TeamRepository;
import com.PuzzleU.Server.university.entity.University;
import com.PuzzleU.Server.interest.repository.InterestRepository;
import com.PuzzleU.Server.location.repository.LocationRepository;
import com.PuzzleU.Server.university.repository.UniversityRepository;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.enumSet.UserRoleEnum;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.common.jwt.JwtUtil;
import com.PuzzleU.Server.user.dto.*;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    // 1. ApiResponseDto 형식을 가진 method를 구현 (Success / Response / Error ) 존재
    //    // 2. Response의 형태가 미정이므로 SuccessResponse로 구현해준다
    //    // 3. 만약에 유저가 존재한다면 RestApiException에 추가 ( 여긴 에러 타입을 넣어야한다) - 중간에 에러가 뜬다면
    // 4. SignupRequestDto 에 있는 Boolean admin이 True일 경우 role은 UserRoleEnum.ADMIN이 된다
    // 5. return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"));
    // 응답은 ResponseUtils를 이용. 성공했으니까 ok 메소드를 써야겠지?
    // ok는 ApiResonseDto를 돌려준다 여기에는 success, error, 가 담긴다
    // 이때 response에는 status와 message가 담기는데 이것들이 HttpStatus.OK, "회원가입 성공" 다.

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SkillsetRepository skillsetRepository;
    private final ExperienceRepository experienceRepository;
    private final UserSkillsetRelationRepository userSkillsetRelationRepository;
    private final MajorRepository majorRepository;
    private final UniversityRepository universityRepository;
    private final ProfileRepository profileRepository;
    private final PositionRepository positionRepository;
    private final InterestRepository interestRepository;
    private final LocationRepository locationRepository;
    private final UserInterestRelationRepository userInterestRelationRepository;
    private final UserLocationRelationRepository userLocationRelationRepository;
    private final FriendshipRepository friendshipRepository;
    private final ApplyRepository applyRepository;
    private final TeamUserRepository teamUserRepository;
    private final TeamLocationRelationRepository teamLocationRelationRepository;
    private final TeamRepository teamRepository;

    // 회원가입 API
    @Transactional
    public ApiResponseDto<SuccessResponse> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new RestApiException(ErrorType.DUPLICATED_USERNAME);
        }

        // 입력한 username, password, admin 으로 user 객체 만들어 repository 에 저장
        UserRoleEnum role = requestDto.getAdmin() ? UserRoleEnum.ADMIN : UserRoleEnum.USER;
        userRepository.save(User.of(username, password, role));

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"), null);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ApiResponseDto<SuccessResponse> login(LoginRequestsDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인 & 비밀번호 확인
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new RestApiException(ErrorType.NOT_MATCHING_INFO);
        }

        // header 에 들어갈 JWT 세팅
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
        String jwtToken = jwtUtil.createToken(user.get().getUsername(), user.get().getRole());
        jwtToken.substring(7);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "로그인 성공"), jwtToken);

    }

    // 유저가 회원가입 후 옵션으로 선택해서 등록가능한 API
    public ApiResponseDto<SuccessResponse> createRegisterOptionalUser(
            UserDetails loginUser,
            UserRegisterOptionalDto userRegisterOptionalDto
    ) {


        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        if (userRegisterOptionalDto.getUniversityStart() == null) {
            System.out.println("null");
        }
        Optional<Major> optionalMajor = majorRepository.findById(userRegisterOptionalDto.getMajorId());

        System.out.println(userRegisterOptionalDto.getMajorId());
        Optional<University> optionalUniversity = universityRepository.findById(userRegisterOptionalDto.getUniversityId());
        System.out.println(optionalUniversity);


        Major major = optionalMajor.orElseThrow(() -> {
            System.out.println("Major not found");
            return new RestApiException(ErrorType.NOT_FOUND_MAJOR);
        });

        University university = optionalUniversity.orElseThrow(() -> {
            System.out.println("University not found");
            return new RestApiException(ErrorType.NOT_FOUND_UNIVERSITY);
        });

        user.setUniversityStart(userRegisterOptionalDto.getUniversityStart());
        user.setUniversityEnd(userRegisterOptionalDto.getUniversityEnd());
        user.setUniversityStatus(userRegisterOptionalDto.getUniversityStatus());
        user.setUserRepresentativeExperience(userRegisterOptionalDto.getUserRepresentativeExperience());
        user.setUserRepresentativeProfileSentence(userRegisterOptionalDto.getUserRepresentativeProfileSentence());
        user.setWorkType(userRegisterOptionalDto.getUserWorkType());
        user.setMajor(major);
        user.setUniversity(university);


        List<UserSkillsetRelation> userSkillsetRelations = new ArrayList<>();
        for (SkillSetDto skillSetDto : userRegisterOptionalDto.getSkillSetDtoList()) {
            Long skillSetId = skillSetDto.getSkillSetId();
            Optional<Skillset> savedSkillset = skillsetRepository.findById(skillSetId);

            if (savedSkillset.isPresent()) {
                UserSkillsetRelation userSkillsetRelation = new UserSkillsetRelation();
                userSkillsetRelation.setUser(user);
                userSkillsetRelation.setSkillset(savedSkillset.get());
                userSkillsetRelation.setLevel(skillSetDto.getSkillSetLevel());
                userSkillsetRelations.add(userSkillsetRelation);
            } else {
                throw new RestApiException(ErrorType.NOT_FOUND_SKILLSET);
            }
        }


        List<Experience> experiences = new ArrayList<>();
        for (ExperienceDto experienceDto : userRegisterOptionalDto.getExperienceDtoList()) {
            Experience experience = new Experience();
            experience.setUser(user);
            experience.setExperienceName(experienceDto.getExperienceName());
            experience.setExperienceStartYear(experienceDto.getExperienceStartYear());
            experience.setExperienceStartMonth(experienceDto.getExperienceStartMonth());
            experience.setExperienceEndYear(experienceDto.getExperienceEndYear());
            experience.setExperienceEndMonth(experienceDto.getExperienceEndMonth());
            experience.setExperienceType(experienceDto.getExperienceType());
            experience.setExperienceStatus(experienceDto.getExperienceStatus());
            experience.setExperienceRole(experienceDto.getExperienceRole());
            experiences.add(experience);
        }


        userRepository.save(user);
        userSkillsetRelationRepository.saveAll(userSkillsetRelations);
        experienceRepository.saveAll(experiences);


        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "선택사항 저장완료"), null);
    }

    // 회원가입 후 필수로 작성해야하는 것들을 등록하는 API
    public ApiResponseDto<SuccessResponse> registerEssential(UserDetails loginUser, UserRegisterEssentialDto userRegisterEssentialDto) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        // 이름 설정
        String userKoreaName = userRegisterEssentialDto.getUserKoreaName();
        if (userKoreaName == null) {
            throw new RestApiException(ErrorType.NAME_NOT_PROVIDED);
        }
        user.setUserKoreaName(userKoreaName);

        // 퍼즐 아이디 설정
        String userPuzzleId = userRegisterEssentialDto.getUserPuzzleId();
        if (userPuzzleId == null) {
            throw new RestApiException(ErrorType.PUZZLE_ID_NOT_PROVIDED);
        }
        user.setUserPuzzleId(userPuzzleId);

        // 프로필 설정
        Long profileId = userRegisterEssentialDto.getUserProfileId();
        if (profileId == null) {
            throw new RestApiException(ErrorType.PROFILE_NOT_PROVIDED);
        }
        Profile profile = profileRepository.findByProfileId(profileId)
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_PROFILE));
        user.setUserProfile(profile);

        // 포지션 설정
        Long positionId1 = userRegisterEssentialDto.getUserPositionId1();
        Long positionId2 = userRegisterEssentialDto.getUserPositionId2();

        if (positionId1 == null) {
            throw new RestApiException(ErrorType.TOO_FEW_POSITIONS);
        }
        Position position1 = positionRepository.findByPositionId(positionId1)
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_POSITION));
        user.setUserPosition1(position1);

        if (positionId2 != null) { // 2순위까지 골랐을 때
            Position position2 = positionRepository.findByPositionId(positionId2)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_POSITION));
            user.setUserPosition2(position2);
        }

        userRepository.save(user);


        // 관심 분야 설정
        for (Long interestId : userRegisterEssentialDto.getUserInterestIdList()) {
            Interest interest = interestRepository.findByInterestId(interestId)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_INTEREST));
            Boolean interestPass = Boolean.FALSE;
            for (UserInterestRelation userInterestRelation : user.getUserInterestRelations()) {
                if (userInterestRelation.getInterest() == interest) {
                    interestPass = Boolean.TRUE; // 이미 있는 관계면 저장하지 않음
                }
            }

            if (interestPass == Boolean.FALSE) {
                UserInterestRelation userInterestRelation = UserInterestRelation.builder()
                        .user(user)
                        .interest(interest).build();
                userInterestRelationRepository.save(userInterestRelation);
            }
        }

        // 지역 설정
        if (userRegisterEssentialDto.getUserLocationIdList().size() > 2) { // 예외 처리: 2개 이상의 지역이 선택되었을 때
            throw new RestApiException(ErrorType.TOO_MUCH_LOCATIONS);
        }

        for (Long locationId : userRegisterEssentialDto.getUserLocationIdList()) {
            Location location = locationRepository.findByLocationId(locationId)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_LOCATION));

            Boolean locationPass = Boolean.FALSE;
            for (UserLocationRelation userLocationRelation : user.getUserLocationRelations()) {
                if (userLocationRelation.getLocation() == location) {
                    locationPass = Boolean.TRUE; // 이미 있는 연관 관계면 저장하지 않음
                }
            }

            if (locationPass == Boolean.FALSE) {
                UserLocationRelation userLocationRelation = UserLocationRelation.builder()
                        .user(user)
                        .location(location).build();
                userLocationRelationRepository.save(userLocationRelation);
            }
        }

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 필수 정보 저장 완료"), null);
    }

    // 모든 멤버들을 검색할 수 있는 API
    public ApiResponseDto<FriendShipSearchResponseDto> searchUser(int pageNo, int pageSize, String sortBy, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<User> users;
        users = new PageImpl<>(userRepository.findByUserKoreaNameContaining(keyword, pageable));
        List<UserSimpleDto> userSimpleDtoList = users.getContent().stream()
                .map(user -> {
                    UserSimpleDto userSimpleDto = new UserSimpleDto();
                    userSimpleDto.setUserId(user.getId());
                    userSimpleDto.setUserProfile(user.getUserProfile());
                    userSimpleDto.setUserKoreaName(user.getUserKoreaName());
                    userSimpleDto.setUserRepresentativeProfileSentence(user.getUserRepresentativeProfileSentence());
                    return userSimpleDto;
                })
                .collect(Collectors.toList());
        FriendShipSearchResponseDto friendShipSearchResponseDto = new FriendShipSearchResponseDto();
        friendShipSearchResponseDto.setUserSimpleDtoList(userSimpleDtoList);
        friendShipSearchResponseDto.setLast(users.isLast());
        friendShipSearchResponseDto.setTotalPages(users.getTotalPages());
        friendShipSearchResponseDto.setPageNo(pageNo);
        friendShipSearchResponseDto.setPageSize(pageSize);
        friendShipSearchResponseDto.setTotalElements(users.getTotalElements());
        return ResponseUtils.ok(friendShipSearchResponseDto, null);
    }

    public ApiResponseDto<SuccessResponse> updateUserProfileBasic(UserDetails loginUser, UserProfileBasicDto userProfileBasicDto) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        Long userProfileId = userProfileBasicDto.getUserProfileId();
        String userKoreaName = userProfileBasicDto.getUserKoreaName();
        Long positionId1 = userProfileBasicDto.getPositionId1();
        Long positionId2 = userProfileBasicDto.getPositionId2();
        WorkType workType = userProfileBasicDto.getWorkType();
        String userRepresentativeProfileSentence = userProfileBasicDto.getUserRepresentativeProfileSentence();

        if (userProfileId != null) { // 프로필 설정
            Profile profile = profileRepository.findByProfileId(userProfileId)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_PROFILE));
            user.setUserProfile(profile);
        }

        if (userKoreaName != null) {
            user.setUserKoreaName(userKoreaName);
        }

        if (positionId1 != null) {
            Position position1 = positionRepository.findByPositionId(positionId1)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_POSITION));
            user.setUserPosition1(position1);
        }

        if (positionId2 != null) {
            Position position2 = positionRepository.findByPositionId(positionId2)
                    .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_POSITION));
            user.setUserPosition2(position2);
        }

        if (workType != null) {
            user.setWorkType(workType);
        }

        if (userRepresentativeProfileSentence != null) {
            user.setUserRepresentativeProfileSentence(userRepresentativeProfileSentence);
        }

        userRepository.save(user);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "프로필 기본 정보 수정 완료"), null);
    }

    // 특정 유저에게 친구신청을 거는 것
    public ApiResponseDto<SuccessResponse> registerFriend(UserDetails loginUser, Long userId) {
        User user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        // friendship이 만들어져야한다
        // 무조건 거는 사람이 user1, 받는 사람이 user2로 되도록
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        // 만약에 user1과 2가 존재한다면 끝
        Optional<FriendShip> friendShip_check = friendshipRepository.findByUser1AndUser2(user1, user2);

        if (friendShip_check.isPresent()) {
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "이미 진행된 친구신청입니다."), null);
        } else {
            // 친구 관계가 존재하지 않는 경우, 새로운 친구 관계 생성
            FriendShip friendShip = new FriendShip();
            friendShip.setUser2(user2);
            friendShip.setUser1(user1);
            friendShip.setUserStatus(false);
            friendshipRepository.save(friendShip);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구신청이 완료되었습니다"), null);
        }

    }
    public ApiResponseDto<SuccessResponse> responseFriend(UserDetails loginUser, Long userId) {
        User user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<FriendShip> friendShip_check = friendshipRepository.findByUser1AndUser2(user1, user2);
        FriendShip friendShip = friendShip_check.orElseThrow(()-> new RestApiException(ErrorType.NOT_FOUND_FRIENDSHIP));
        friendShip.setUserStatus(true);
        friendshipRepository.save(friendShip);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구신청이 완료되었습니다"), null);

    }
    public ApiResponseDto<SuccessResponse> deleteFriend(UserDetails loginUser, Long userId) {
        User user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<FriendShip> friendShip_check = friendshipRepository.findByUser1AndUser2(user1, user2);
        FriendShip friendShip = friendShip_check.orElseThrow(()-> new RestApiException(ErrorType.NOT_FOUND_FRIENDSHIP));
        friendshipRepository.delete(friendShip);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구신청이 완료되었습니다"), null);

    }
    public ApiResponseDto<TeamListDto> getApplyType(UserDetails loginUser, int pageNo, int pageSize, String sortBy, String type) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Team> teamPage;
        if ("total".equals(type)) {
            teamPage = new PageImpl<>(applyRepository.findByUser(user, pageable));
        } else if ("wait".equals(type)) {
            teamPage = new PageImpl<>(applyRepository.findByUserAndApplyStatusIsWaiting(user, pageable));
        } else if ("end".equals(type)) {
            teamPage = new PageImpl<>(applyRepository.findByUserAndApplyStatusIsFinished(user, pageable));
        } else {
            throw new RestApiException(ErrorType.NAME_NOT_PROVIDED);
        }

        TeamListDto teamListDto = new TeamListDto();
        List<TeamAbstractDto> teamAbstractDtos = new ArrayList<>();
        teamAbstractDtos = teamPage.getContent().stream()
                .map(team ->
                {
                    TeamAbstractDto teamAbstractDto = new TeamAbstractDto();
                    List<TeamUserRelation> teamUserRelation = teamUserRepository.findByTeam(team);
                    for (TeamUserRelation teamuserRelation : teamUserRelation) {
                        if (teamuserRelation.getIsWriter())
                        {
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
        teamListDto.setTotalTeam(teamAbstractDtos.size());
        teamListDto.setTeamList(teamAbstractDtos);
        teamListDto.setLast(teamPage.isLast());
        teamListDto.setTotalPages(teamPage.getTotalPages());
        teamListDto.setTotalElements(teamPage.getTotalElements());
        teamListDto.setPageNo(pageNo);
        teamListDto.setPageSize(pageSize);


        return ResponseUtils.ok(teamListDto, null);
    }
    public ApiResponseDto<ApplyTeamDto> getApply(UserDetails loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        ApplyTeamDto applyTeamDto = new ApplyTeamDto();

        for (int i = 0; i <= 1; i++) {
            List<Team> team1;
            Team team2;
            if (i == 0) {
                team1 = applyRepository.findFirstByUserAndApplyStatusIsWaitingOne(user);
                if (team1.isEmpty()) {
                    team2 = new Team(); // 빈 팀 생성
                } else {
                    team2 = team1.get(0);
                }
            } else {
                team1 = applyRepository.findFirstByUserAndApplyStatusIsFinishedOne(user);
                if (team1.isEmpty()) {
                    team2 = new Team(); // 빈 팀 생성
                } else {
                    team2 = team1.get(0);
                }
            }
            TeamAbstractDto teamAbstractDto1 = new TeamAbstractDto();
            if (!team1.isEmpty()) {
                List<TeamUserRelation> teamUserRelation1 = teamUserRepository.findByTeam(team2);
                for (TeamUserRelation teamuserRelation : teamUserRelation1) {
                    if (teamuserRelation.getIsWriter()) {
                        teamAbstractDto1.setTeamWriter(teamuserRelation.getUser().getUserKoreaName());
                        break;
                    }
                }
                List<TeamLocationRelation> teamLocationRelation = teamLocationRelationRepository.findByTeam(team2);
                List<String> locationList1 = new ArrayList<>();
                for (TeamLocationRelation teamLocationRelation1 : teamLocationRelation) {
                    String location = teamLocationRelation1.getLocation().getLocationName();
                    locationList1.add(location);
                }
                teamAbstractDto1.setTeamId(team2.getTeamId());
                teamAbstractDto1.setTeamNeed(team2.getTeamMemberNeed());
                teamAbstractDto1.setTeamNowMember(team2.getTeamMemberNow());
                teamAbstractDto1.setTeamTitle(team2.getTeamTitle());
                teamAbstractDto1.setTeamPoster(team2.getCompetition().getCompetitionPoster());
                teamAbstractDto1.setTeamLocations(locationList1);
                List<String> PositionList1 = new ArrayList<>();
                for (Position position : team2.getPositionList()) {
                    PositionList1.add(position.getPositionName());
                }
                teamAbstractDto1.setPositionList(PositionList1);
            }
            if (i == 0) {
                applyTeamDto.setTeamListDto1(teamAbstractDto1);
            } else {
                applyTeamDto.setTeamListDto2(teamAbstractDto1);
            }
        }
        return ResponseUtils.ok(applyTeamDto, null);
    }

    public ApiResponseDto<SuccessResponse> deleteApply(UserDetails loginUser, Long apply_id)
    {
        Optional<Apply> applyOptional = applyRepository.findById(apply_id);
        Apply apply = applyOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND_APPLY)
        );
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        if(apply.getUser() == user)
        {
            applyRepository.delete(apply);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "지원서 삭제완료"),null);
        }
        else
        {
            return ResponseUtils.error(ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE,"권한이 없습니다"));
        }

    }
    public ApiResponseDto<TeamApplyDto> getTeamApplyTotal(UserDetails loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        TeamApplyDto teamApplyDto = new TeamApplyDto();

        for (int i = 0; i <= 1; i++) {
            List<Team> team1;
            Team team2;
            if (i == 0) {
                team1 = teamUserRepository.findByUserAndWaitingOne(user);
                if (team1.isEmpty()) {
                    team2 = new Team(); // 빈 팀 생성
                } else {
                    team2 = team1.get(0);
                }
            } else {
                team1 = teamUserRepository.findByUserAndEndOne(user);
                if (team1.isEmpty()) {
                    team2 = new Team(); // 빈 팀 생성
                } else {
                    team2 = team1.get(0);
                }
            }
            TeamAbstractDto teamAbstractDto1 = new TeamAbstractDto();
            if (!team1.isEmpty()) {
                List<TeamUserRelation> teamUserRelation1 = teamUserRepository.findByTeam(team2);
                for (TeamUserRelation teamuserRelation : teamUserRelation1) {
                    if (teamuserRelation.getIsWriter()) {
                        teamAbstractDto1.setTeamWriter(teamuserRelation.getUser().getUserKoreaName());
                        break;
                    }
                }
                List<TeamLocationRelation> teamLocationRelation = teamLocationRelationRepository.findByTeam(team2);
                List<String> locationList1 = new ArrayList<>();
                for (TeamLocationRelation teamLocationRelation1 : teamLocationRelation) {
                    String location = teamLocationRelation1.getLocation().getLocationName();
                    locationList1.add(location);
                }
                teamAbstractDto1.setTeamId(team2.getTeamId());
                teamAbstractDto1.setTeamNeed(team2.getTeamMemberNeed());
                teamAbstractDto1.setTeamNowMember(team2.getTeamMemberNow());
                teamAbstractDto1.setTeamTitle(team2.getTeamTitle());
                teamAbstractDto1.setTeamPoster(team2.getCompetition().getCompetitionPoster());
                teamAbstractDto1.setTeamLocations(locationList1);
                List<String> PositionList1 = new ArrayList<>();
                for (Position position : team2.getPositionList()) {
                    PositionList1.add(position.getPositionName());
                }
                teamAbstractDto1.setPositionList(PositionList1);
            }
            if (i == 0) {
                teamApplyDto.setTeamAbstractDtoWaiting(teamAbstractDto1);
            } else {
                teamApplyDto.setTeamAbstractDtoEnd(teamAbstractDto1);
            }
        }
        return ResponseUtils.ok(teamApplyDto, null);
    }
    public ApiResponseDto<TeamListDto> getTeamApplyType(UserDetails loginUser, int pageNo, int pageSize, String sortBy, String type) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Team> teamPage;
        if ("total".equals(type)) {
            teamPage = new PageImpl<>(applyRepository.findByUser(user, pageable));
        } else if ("wait".equals(type)) {
            teamPage = new PageImpl<>(applyRepository.findByUserAndApplyStatusIsWaiting(user, pageable));
        } else if ("end".equals(type)) {
            teamPage = new PageImpl<>(applyRepository.findByUserAndApplyStatusIsFinished(user, pageable));
        } else {
            throw new RestApiException(ErrorType.NAME_NOT_PROVIDED);
        }

        TeamListDto teamListDto = new TeamListDto();
        List<TeamAbstractDto> teamAbstractDtos = teamPage.getContent().stream()
                .map(team ->
                {
                    TeamAbstractDto teamAbstractDto = new TeamAbstractDto();
                    List<TeamUserRelation> teamUserRelation = teamUserRepository.findByTeam(team);
                    for (TeamUserRelation teamuserRelation : teamUserRelation) {
                        if (teamuserRelation.getIsWriter()) {
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
                    for (Position position : team.getPositionList()) {
                        PositionList.add(position.getPositionName());
                    }
                    teamAbstractDto.setPositionList(PositionList);
                    return teamAbstractDto;
                })
                .collect(Collectors.toList());

        teamListDto.setTotalTeam(teamAbstractDtos.size());
        teamListDto.setTeamList(teamAbstractDtos);
        teamListDto.setLast(teamPage.isLast());
        teamListDto.setTotalPages(teamPage.getTotalPages());
        teamListDto.setTotalElements(teamPage.getTotalElements());
        teamListDto.setPageNo(pageNo);
        teamListDto.setPageSize(pageSize);

        return ResponseUtils.ok(teamListDto, null);
    }
    public ApiResponseDto<SuccessResponse> teamStatus(UserDetails loginUser, Long team_id, TeamStatusDto teamStatusDto)
    {
        Optional<Team> teamOptional  = teamRepository.findById(team_id);
        Team team = teamOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND_TEAM)
        );
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        // 유저가 권한이 없거나
        Optional<TeamUserRelation> teamUserRelationOptional = teamUserRepository.findByUserAndTeam(user, team);
        TeamUserRelation teamUserRelation = teamUserRelationOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND_USER_TEAM)
        );
        if(teamUserRelation.getIsWriter()==true)
        {
            team.setTeamStatus(teamStatusDto.getTeamStatus());
            teamRepository.save(team);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "팀 공고글 상태 변경완료"),null);
        }
        else
        {
            return ResponseUtils.error(ErrorResponse.of(HttpStatus.NOT_ACCEPTABLE,"권한이 없습니다"));
        }

    }

    // 내 프로필 보기
    public ApiResponseDto<UserMyProfileDto> readMyProfile(UserDetails loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        if (user.getUserKoreaName() == null) {
            throw new RestApiException(ErrorType.NAME_NOT_PROVIDED); // 필수 회원가입 정보가 없으면 에러 처리
        }

        if (user.getUserPuzzleId() == null) {
            throw new RestApiException(ErrorType.PUZZLE_ID_NOT_PROVIDED); // 필수 회원가입 정보가 없으면 에러 처리
        }

        if (user.getUserPosition1() == null) {
            throw  new RestApiException(ErrorType.POSITION_NOT_PROVIDED); // 필수 회원가입 정보가 없으면 에러 처리
        }

        if (user.getUserProfile() == null) {
            throw new RestApiException(ErrorType.PROFILE_NOT_PROVIDED); // 필수 회원가입 정보가 없으면 에러 처리
        }


        Profile profile = user.getUserProfile();

        ProfileDto profileDto = ProfileDto.builder()
                .ProfileId(profile.getProfileId())
                .ProfileUrl(profile.getProfielUrl()).build();

        Position position1 = user.getUserPosition1();

        PositionDto positionDto1 = PositionDto.builder()
                .PositionId(position1.getPositionId())
                .PositionName(position1.getPositionName()).build();

        Position position2 = user.getUserPosition2();
        PositionDto positionDto2 = new PositionDto();
        if (position2 == null) {
            positionDto2 = null;
        }
        else {
                    positionDto2 = PositionDto.builder()
                    .PositionId(position2.getPositionId())
                    .PositionName(position2.getPositionName()).build();
        }

        University university = user.getUniversity();
        Major major = user.getMajor();

        UserProfileUniversityDto  userProfileUniversityDto = new UserProfileUniversityDto();
        if (university == null) {
            userProfileUniversityDto = null;
        }
        else if (major == null) {
            userProfileUniversityDto = null;
        }
        else {
            userProfileUniversityDto = UserProfileUniversityDto.builder()
                    .UniversityId(university.getUniversityId())
                    .UniversityName(university.getUniversityName())
                    .MajorId(major.getMajorId())
                    .MajorName(major.getMajorName())
                    .UserUniversityStatus(user.getUniversityStatus())
                    .UserUniversityStart(user.getUniversityStart())
                    .UserUniversityEnd(user.getUniversityEnd()).build();
        }

        List<Experience> userExperienceList = user.getExperience();
        List<UserProfileExperienceDto> userProfileExperienceDtoList = userExperienceList.stream()
                .map(e -> UserProfileExperienceDto.builder()
                        .ExperienceId(e.getExperienceId())
                        .ExperienceType(e.getExperienceType())
                        .ExperienceName(e.getExperienceName()).build())
                .collect(Collectors.toList());

        List<UserSkillsetRelation> userSkillsetRelationList = user.getUserSkillsetRelations();
        List<UserProfileSkillsetDto> userProfileSkillsetDtoList = new ArrayList<>();

        for (UserSkillsetRelation rel : userSkillsetRelationList) {
            Skillset skillset = rel.getSkillset();
            Level level = rel.getLevel();

            UserProfileSkillsetDto userProfileSkillsetDto = UserProfileSkillsetDto.builder()
                    .SkillsetId(skillset.getSkillsetId())
                    .SkillsetName(skillset.getSkillsetName())
                    .SkillsetLogo(skillset.getSkillsetLogo())
                    .SKillsetLevel(level).build();

            userProfileSkillsetDtoList.add(userProfileSkillsetDto);
        }


        UserMyProfileDto userMyProfileDto = new UserMyProfileDto();


        userMyProfileDto.setUserId(user.getId());
        userMyProfileDto.setUserProfile(profileDto);
        userMyProfileDto.setUserKoreaName(user.getUserKoreaName());
        userMyProfileDto.setUserPuzzleId(user.getUserPuzzleId());
        userMyProfileDto.setPosition1(positionDto1);
        userMyProfileDto.setPosition2(positionDto2);
        userMyProfileDto.setWorkType(user.getWorkType());
        userMyProfileDto.setUserRepresentativeProfileSentence(user.getUserRepresentativeProfileSentence());
        userMyProfileDto.setUserUniversity(userProfileUniversityDto);
        userMyProfileDto.setExperienceList(userProfileExperienceDtoList);
        userMyProfileDto.setSkillsetList(userProfileSkillsetDtoList);

        return ResponseUtils.ok(userMyProfileDto, null);
    }

}




