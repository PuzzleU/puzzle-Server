package com.PuzzleU.Server.dto.user;

import com.PuzzleU.Server.entity.profile.Profile;
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
