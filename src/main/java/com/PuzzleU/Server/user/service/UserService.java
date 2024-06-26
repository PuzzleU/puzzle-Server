package com.PuzzleU.Server.user.service;

import com.PuzzleU.Server.apply.repository.ApplyRepository;
import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ErrorResponse;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.*;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.common.jwt.JwtUtil;
import com.PuzzleU.Server.common.jwt.TokenDto;
import com.PuzzleU.Server.experience.dto.ExperienceDto;
import com.PuzzleU.Server.experience.entity.Experience;
import com.PuzzleU.Server.experience.repository.ExperienceRepository;
import com.PuzzleU.Server.friendship.dto.FriendShipSearchResponseDto;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.friendship.repository.FriendshipRepository;
import com.PuzzleU.Server.interest.entity.Interest;
import com.PuzzleU.Server.interest.repository.InterestRepository;
import com.PuzzleU.Server.location.entity.Location;
import com.PuzzleU.Server.location.repository.LocationRepository;
import com.PuzzleU.Server.major.entity.Major;
import com.PuzzleU.Server.major.repository.MajorRepository;
import com.PuzzleU.Server.position.dto.PositionDto;
import com.PuzzleU.Server.position.entity.Position;
import com.PuzzleU.Server.position.repository.PositionRepository;
import com.PuzzleU.Server.profile.dto.ProfileDto;
import com.PuzzleU.Server.profile.entity.Profile;
import com.PuzzleU.Server.profile.repository.ProfileRepository;
import com.PuzzleU.Server.relations.entity.UserInterestRelation;
import com.PuzzleU.Server.relations.entity.UserLocationRelation;
import com.PuzzleU.Server.relations.entity.UserSkillsetRelation;
import com.PuzzleU.Server.relations.entity.UserUniversityRelation;
import com.PuzzleU.Server.relations.repository.*;
import com.PuzzleU.Server.skillset.dto.SkillSetDto;
import com.PuzzleU.Server.skillset.entity.Skillset;
import com.PuzzleU.Server.skillset.repository.SkillsetRepository;
import com.PuzzleU.Server.team.repository.TeamRepository;
import com.PuzzleU.Server.university.dto.UniversityRegistDto;
import com.PuzzleU.Server.university.entity.University;
import com.PuzzleU.Server.university.repository.UniversityRepository;
import com.PuzzleU.Server.user.dto.*;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserUniversityRelationRepository userUniversityRelationRepository;

    // 회원가입 API
    @Transactional
    public ApiResponseDto<TokenDto> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new RestApiException(ErrorType.DUPLICATED_USERNAME);
        }

        // 입력한 username, password, admin 으로 user 객체 만들어 repository 에 저장
        UserRoleEnum role = requestDto.getAdmin() ? UserRoleEnum.ADMIN : UserRoleEnum.USER;
        User user = User.of(LoginType.NONE, username, password, role);
        userRepository.saveAndFlush(user); // 카카오로 임의 설정

        TokenDto tokenDto = new TokenDto();
        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole());

        tokenDto.setMessage("회원가입 성공");
        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);

        return ResponseUtils.ok(tokenDto,ErrorResponse.builder().status(200).message("요청 성공").build());
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
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "로그인 성공"), ErrorResponse.builder().build());

    }

    // 유저가 회원가입 후 옵션으로 선택해서 등록가능한 API
    @Transactional
    public ApiResponseDto<SuccessResponse> createRegisterOptionalUser(
            UserDetails loginUser,
            UserRegisterOptionalDto userRegisterOptionalDto

    ) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        for(UniversityRegistDto universityRegistDtoEach : userRegisterOptionalDto.getUniversityRegistDtoList())
        {
            Optional<University> optionalUniversity = Optional.empty();
            if (universityRegistDtoEach.getUniversityId() != null) {
                optionalUniversity = universityRepository.findById(universityRegistDtoEach.getUniversityId());
            }
            University university = optionalUniversity.orElse(null);
            if (university == null) {
                throw new RestApiException(ErrorType.NOT_FOUND_UNIVERSITY);
            }
            UserUniversityRelation userUniversityRelation = new UserUniversityRelation();
            userUniversityRelation.setUniversity(university);
            userUniversityRelation.setUser(user);
            userUniversityRelation.setUniversityEnd(universityRegistDtoEach.getUniversityEnd());
            userUniversityRelation.setUniversityStart(universityRegistDtoEach.getUniversityStart());
            if (universityRegistDtoEach.getUniversityEnd() < universityRegistDtoEach.getUniversityStart())
            {
                throw new RestApiException(ErrorType.NOT_ALLOWED_YEAR);
            }userUniversityRelation.setUniversityStatus(universityRegistDtoEach.getUniversityStatus());
            Optional<Major> optionalMajor = Optional.empty();
            if (universityRegistDtoEach.getMajorId() != null) {
                optionalMajor = majorRepository.findById(universityRegistDtoEach.getMajorId());
            }
            Major major = optionalMajor.orElse(null);
            if (major == null) {
                throw new RestApiException(ErrorType.NOT_FOUND_MAJOR);
            }
            userUniversityRelation.setMajorId(major.getMajorId());
            userUniversityRelation.setMajorName(major.getMajorName());
            userUniversityRelationRepository.save(userUniversityRelation);
        }



        user.setUserRepresentativeExperience(userRegisterOptionalDto.getUserRepresentativeExperience());
        user.setUserRepresentativeProfileSentence(userRegisterOptionalDto.getUserRepresentativeProfileSentence());
        user.setWorkType(userRegisterOptionalDto.getUserWorkType());



        List<UserSkillsetRelation> userSkillsetRelations = new ArrayList<>();
        if (userRegisterOptionalDto.getSkillSetDtoList() != null) {
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
        }

        List<Experience> experiences = new ArrayList<>();
        if (userRegisterOptionalDto.getExperienceDtoList() != null) {
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
        }

        userRepository.save(user);
        userSkillsetRelationRepository.saveAll(userSkillsetRelations);
        experienceRepository.saveAll(experiences);

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "선택사항 저장완료"), ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    @org.springframework.transaction.annotation.Transactional
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

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 필수 정보 저장 완료"), ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    // 회원가입 시 퍼즐 ID 중복 여부 반환
    public ApiResponseDto<SuccessResponse> getPuzzleIdDuplicate(String puzzleId) {
        Optional<User> userOptional = userRepository.findByUserPuzzleId(puzzleId);

        if (userOptional.isPresent()) {
            throw new RestApiException(ErrorType.DUPLICATED_PUZZLE_ID);
        }

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "사용할 수 있는 퍼즐 ID입니다."), ErrorResponse.builder().status(200).message("요청 성공").build());

    }

    @Transactional
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
        return ResponseUtils.ok(friendShipSearchResponseDto, ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    @Transactional
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
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "프로필 기본 정보 수정 완료"), ErrorResponse.builder().status(200).message("요청 성공").build());
    }




    @Transactional
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


        List<UniversityRegistDto>  universityRegistDtoList = new ArrayList<>();

        List<UserUniversityRelation> userUniversityRelations = userUniversityRelationRepository.findByUser(user);
        for(UserUniversityRelation userUniversityRelation : userUniversityRelations)
        {
            UniversityRegistDto universityRegistDto = new UniversityRegistDto();
            universityRegistDto.setUniversityEnd(userUniversityRelation.getUniversityEnd());
            universityRegistDto.setUniversityStart(userUniversityRelation.getUniversityStart());
            universityRegistDto.setUniversityId(userUniversityRelation.getUserUniversityId());
            universityRegistDto.setMajorId(userUniversityRelation.getMajorId());
            universityRegistDto.setMajorName(userUniversityRelation.getMajorName());
            universityRegistDto.setUniversityStatus(userUniversityRelation.getUniversityStatus());
            universityRegistDto.setUniversityType(userUniversityRelation.getUniversity().getUniversityType());
            universityRegistDtoList.add(universityRegistDto);
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
        userMyProfileDto.setUniversityRegistDtoList(universityRegistDtoList);
        userMyProfileDto.setExperienceList(userProfileExperienceDtoList);
        userMyProfileDto.setSkillsetList(userProfileSkillsetDtoList);

        return ResponseUtils.ok(userMyProfileDto, ErrorResponse.builder().status(200).message("요청 성공").build());
    }
    public ApiResponseDto<List<UserSimpleDto>> userSearch(UserDetails loginUser, int pageNo, int pageSize, String sortBy, String search) {
        User loginuser = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<User> userPage;
        userPage = new PageImpl<>(userRepository.findAllExcept(loginuser, pageable));

        List<UserSimpleDto> userSimpleDtoList = userPage.getContent().stream()
                .map(user -> {
                    UserSimpleDto userSimpleDto = new UserSimpleDto();
                    userSimpleDto.setUserProfile(user.getUserProfile());
                    userSimpleDto.setUserKoreaName(user.getUserKoreaName());
                    userSimpleDto.setUserId(user.getId());
                    userSimpleDto.setUserRepresentativeProfileSentence(user.getUserRepresentativeProfileSentence());
                    return userSimpleDto;
                })
                .collect(Collectors.toList());

        return ResponseUtils.<List<UserSimpleDto>>ok(userSimpleDtoList, ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    @Transactional
    // 유저 프로필 보기
    public ApiResponseDto<UserProfileDto> readUserProfile(UserDetails loginUser, Long profileUserId) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        User profileUser = userRepository.findById(profileUserId)
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));



        if (profileUser.getUserKoreaName() == null) {
            throw new RestApiException(ErrorType.NAME_NOT_PROVIDED); // 필수 회원가입 정보가 없으면 에러 처리
        }

        if (profileUser.getUserPuzzleId() == null) {
            throw new RestApiException(ErrorType.PUZZLE_ID_NOT_PROVIDED); // 필수 회원가입 정보가 없으면 에러 처리
        }

        if (profileUser.getUserPosition1() == null) {
            throw  new RestApiException(ErrorType.POSITION_NOT_PROVIDED); // 필수 회원가입 정보가 없으면 에러 처리
        }

        if (profileUser.getUserProfile() == null) {
            throw new RestApiException(ErrorType.PROFILE_NOT_PROVIDED); // 필수 회원가입 정보가 없으면 에러 처리
        }


        Profile profile = profileUser.getUserProfile();

        ProfileDto profileDto = ProfileDto.builder()
                .ProfileId(profile.getProfileId())
                .ProfileUrl(profile.getProfielUrl()).build();

        Position position1 = profileUser.getUserPosition1();

        PositionDto positionDto1 = PositionDto.builder()
                .PositionId(position1.getPositionId())
                .PositionName(position1.getPositionName()).build();

        Position position2 = profileUser.getUserPosition2();
        PositionDto positionDto2 = new PositionDto();
        if (position2 == null) {
            positionDto2 = null;
        }
        else {
            positionDto2 = PositionDto.builder()
                    .PositionId(position2.getPositionId())
                    .PositionName(position2.getPositionName()).build();
        }

        List<UniversityRegistDto>  universityRegistDtoList = new ArrayList<>();

        List<UserUniversityRelation> userUniversityRelations = userUniversityRelationRepository.findByUser(user);
        for(UserUniversityRelation userUniversityRelation : userUniversityRelations)
        {
            UniversityRegistDto universityRegistDto = new UniversityRegistDto();
            universityRegistDto.setUniversityEnd(userUniversityRelation.getUniversityEnd());
            universityRegistDto.setUniversityStart(userUniversityRelation.getUniversityStart());
            universityRegistDto.setUniversityId(userUniversityRelation.getUserUniversityId());
            universityRegistDto.setMajorId(userUniversityRelation.getMajorId());
            universityRegistDto.setMajorName(userUniversityRelation.getMajorName());
            universityRegistDto.setUniversityStatus(userUniversityRelation.getUniversityStatus());
            universityRegistDto.setUniversityType(userUniversityRelation.getUniversity().getUniversityType());
            universityRegistDtoList.add(universityRegistDto);
        }

        List<Experience> userExperienceList = profileUser.getExperience();
        List<UserProfileExperienceDto> userProfileExperienceDtoList = userExperienceList.stream()
                .map(e -> UserProfileExperienceDto.builder()
                        .ExperienceId(e.getExperienceId())
                        .ExperienceType(e.getExperienceType())
                        .ExperienceName(e.getExperienceName()).build())
                .collect(Collectors.toList());

        List<UserSkillsetRelation> userSkillsetRelationList = profileUser.getUserSkillsetRelations();
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

        // 친구 여부
        FriendStatus friendStatus = FriendStatus.NOT_FRIEND;
        List<FriendShip> friendShipList1 = user.getFriendShip1();
        List<FriendShip> friendShipList2 = user.getFriendShip2();

        for (FriendShip friendShip : friendShipList1) {
            if (friendShip.getUser2() == profileUser) { // 로그인한 유저가 친구 요청을 한 경우
                if (friendShip.getUserStatus() == Boolean.TRUE) { // 프로필 유저와 친구인 상태
                    friendStatus = FriendStatus.FRIEND;
                }
                else if (friendShip.getUserStatus() == Boolean.FALSE) { // 프로필 유저에게 친구 신청을 보내고, 아직 수락하지 않은 상태(대기중)
                    friendStatus = FriendStatus.WAITING;
                }
            }
        }

        for (FriendShip friendShip : friendShipList2) {
            if (friendShip.getUser1() == profileUser) { // 로그인한 유저가 친구 신청을 받은 경우
                if (friendShip.getUserStatus() == Boolean.TRUE) { // 프로필 유저와 친구인 상태
                    friendStatus = FriendStatus.FRIEND;
                }
                else if (friendShip.getUserStatus() == Boolean.FALSE) { // 친구 신청을 받고 수락하지 않은 상태
                    friendStatus = FriendStatus.REQUEST_RECEIVED;
                }
            }
        }

        UserProfileDto userProfileDto = new UserProfileDto();

        userProfileDto.setFriendStatus(friendStatus);

        userProfileDto.setUserId(profileUser.getId());
        userProfileDto.setUserProfile(profileDto);
        userProfileDto.setUserKoreaName(profileUser.getUserKoreaName());
        userProfileDto.setUserPuzzleId(profileUser.getUserPuzzleId());
        userProfileDto.setPosition1(positionDto1);
        userProfileDto.setPosition2(positionDto2);
        userProfileDto.setWorkType(profileUser.getWorkType());
        userProfileDto.setUserRepresentativeProfileSentence(profileUser.getUserRepresentativeProfileSentence());
        userProfileDto.setUniversityRegistDtoList(universityRegistDtoList);
        userProfileDto.setExperienceList(userProfileExperienceDtoList);
        userProfileDto.setSkillsetList(userProfileSkillsetDtoList);

        return ResponseUtils.ok(userProfileDto, ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    // 정보 수신 여부 동의 수정
    public ApiResponseDto<SuccessResponse> updateTermsConsent(UserDetails loginUser, TermsConsentDto termsConsentDto) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        if (!termsConsentDto.getAgeTermConsent() || !termsConsentDto.getServiceTermConsent()
                || !termsConsentDto.getPersonalInfoConsent() || !termsConsentDto.getServiceNotificationConsent()) {
            throw new RestApiException(ErrorType.REQUIRED_TERM_NOT_AGREED);
        }

        user.setAgeTermConsent(termsConsentDto.getAgeTermConsent());
        user.setServiceTermConsent(termsConsentDto.getServiceTermConsent());
        user.setPersonalInfoConsent(termsConsentDto.getPersonalInfoConsent());
        user.setServiceNotificationConsent(termsConsentDto.getServiceNotificationConsent());
        user.setReceiveMarketingConsent(termsConsentDto.getReceiveMarketingConsent());

        userRepository.save(user);

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "정보 수신 여부 저장 완료"), ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    public ApiResponseDto<List<TermDto>> getTermsConsent() {
        List<TermDto> termDtoList = new ArrayList<>();

        TermDto term1 = TermDto.builder()
                .termAlias(Term.AGE_TERM.getTermAlias())
                .termType(Term.AGE_TERM.getTermType())
                .termContent(Term.AGE_TERM.getTermContent()).build();
        termDtoList.add(term1);


        TermDto term2 = TermDto.builder()
                .termAlias(Term.SERVICE_TERM.getTermAlias())
                .termType(Term.SERVICE_TERM.getTermType())
                .termContent(Term.SERVICE_TERM.getTermContent()).build();
        termDtoList.add(term2);

        TermDto term3 = TermDto.builder()
                .termAlias(Term.PERSONAL_INFO_TERM.getTermAlias())
                .termType(Term.PERSONAL_INFO_TERM.getTermType())
                .termContent(Term.PERSONAL_INFO_TERM.getTermContent()).build();
        termDtoList.add(term3);

        TermDto term4 = TermDto.builder()
                .termAlias(Term.SERVICE_NOTIFICATION_TERM.getTermAlias())
                .termType(Term.SERVICE_NOTIFICATION_TERM.getTermType())
                .termContent(Term.SERVICE_NOTIFICATION_TERM.getTermContent()).build();
        termDtoList.add(term4);

        TermDto term5 = TermDto.builder()
                .termAlias(Term.RECEIVE_MARKETING_TERM.getTermAlias())
                .termType(Term.RECEIVE_MARKETING_TERM.getTermType())
                .termContent(Term.RECEIVE_MARKETING_TERM.getTermContent()).build();
        termDtoList.add(term5);

        return ResponseUtils.ok(termDtoList, ErrorResponse.builder().status(200).message("요청 성공").build());
    }

    public User getUserFromAuth()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Optional<User> user = userRepository.findByUsername(name);
        return user.get();
    }
}





