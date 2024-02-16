package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.WorkType;
import com.PuzzleU.Server.position.dto.PositionDto;
import com.PuzzleU.Server.profile.dto.ProfileDto;
import com.PuzzleU.Server.profile.entity.Profile;
import com.PuzzleU.Server.university.dto.UniversityRegistDto;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMyProfileDto {
    private Long UserId;
    private ProfileDto UserProfile;
    private String UserKoreaName;
    private String UserPuzzleId;
    private PositionDto Position1;
    private PositionDto Position2;
    private WorkType WorkType;
    private String UserRepresentativeProfileSentence;
    private List<UniversityRegistDto> universityRegistDtoList;
    private List<UserProfileExperienceDto> ExperienceList;
    private List<UserProfileSkillsetDto> SkillsetList;
}
