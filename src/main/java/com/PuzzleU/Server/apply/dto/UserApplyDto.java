package com.PuzzleU.Server.apply.dto;

import com.PuzzleU.Server.profile.entity.Profile;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserApplyDto {
    private Long UserId;
    private Profile UserProfile;
    private String UserKoreaName;
    private String UserRepresentativeProfileSentence;

    private Long ApplyId;
    private String ApplyTitle;
}
