package com.PuzzleU.Server.service.User;

import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.dto.experience.ExperienceDto;
import com.PuzzleU.Server.dto.skillset.SkillSetDto;
import com.PuzzleU.Server.dto.user.UserRegisterOptionalDto;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.PuzzleU.Server.entity.enumSet.Level;
import com.PuzzleU.Server.entity.experience.Experience;
import com.PuzzleU.Server.entity.major.Major;
import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import com.PuzzleU.Server.entity.skillset.Skillset;
import com.PuzzleU.Server.entity.university.University;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.exception.RestApiException;
import com.PuzzleU.Server.repository.*;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.expression.Each;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegisterOptionalService
{
    private final UserRepository userRepository;
    private final SkillsetRepository skillsetRepository;
    private final ExperienceRepository experienceRepository;
    private final UserSkillsetRelationRepository userSkillsetRelationRepository;
    private final MajorRepository majorRepository;
    private final UniversityRepository universityRepository;

    // 유저정보 가져오고,
    // 유저에 대학정보 저장해주고
    // 스킬셋이랑 경험 리스트 받아와서
    // 각각에 대한 id 값과 여러개 받아와서
    // relation에 하나씩 저장해주기


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
