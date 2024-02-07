package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.profile.entity.Profile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserSimpleDto {
    private String UserKoreaName;
    private Profile userProfile;

    private String userRepresentativeProfileSentence;

    private Long userId;

}
