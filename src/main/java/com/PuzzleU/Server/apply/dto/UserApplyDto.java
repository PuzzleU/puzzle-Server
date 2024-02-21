package com.PuzzleU.Server.apply.dto;

import com.PuzzleU.Server.profile.dto.ProfileDto;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserApplyDto {
    private Long UserId;
    private ProfileDto UserProfile;
    private String UserKoreaName;
    private String UserRepresentativeProfileSentence;

    private Long ApplyId;
    private String ApplyTitle;
}
