package com.PuzzleU.Server.service.User;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.experience.ExperienceDto;
import com.PuzzleU.Server.dto.skillset.SkillSetDto;
import com.PuzzleU.Server.dto.user.LoginRequestsDto;
import com.PuzzleU.Server.dto.user.SignupRequestDto;
import com.PuzzleU.Server.dto.user.UserRegisterOptionalDto;
import com.PuzzleU.Server.entity.experience.Experience;
import com.PuzzleU.Server.entity.major.Major;
import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import com.PuzzleU.Server.entity.skillset.Skillset;
import com.PuzzleU.Server.entity.university.University;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.entity.enumSet.UserRoleEnum;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.jwt.JwtUtil;
import com.PuzzleU.Server.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "로그인 성공"),jwtToken);

    }
    public ApiResponseDto<SuccessResponse> createRegisterOptionalUser(
            Long userId,
            UserRegisterOptionalDto userRegisterOptionalDto
    ) {
        System.out.println("userId:" + userId);

        if (userId == null) {
            throw new RestApiException(ErrorType.NOT_FOUND_USER);
        }
        System.out.println(userRegisterOptionalDto);
        Optional<User> optionalUser = userRepository.findById(userId);
        if(userRegisterOptionalDto.getUniversityStart() == null)
        {
            System.out.println("null");
        }
        Optional<Major> optionalMajor = majorRepository.findById(userRegisterOptionalDto.getMajorId());

        System.out.println(userRegisterOptionalDto.getMajorId());
        Optional<University> optionalUniversity = universityRepository.findById(userRegisterOptionalDto.getUniversityId());
        System.out.println(optionalUniversity);

        User user = optionalUser.orElseThrow(() -> {
            System.out.println("User not found");
            return new RestApiException(ErrorType.NOT_MATCHING_INFO);
        });

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
        user.setUserNudge(userRegisterOptionalDto.getUserNudge());
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


}
