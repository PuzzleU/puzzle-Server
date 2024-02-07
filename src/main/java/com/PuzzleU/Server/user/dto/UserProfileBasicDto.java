package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.WorkType;
import lombok.*;
import org.hibernate.usertype.UserType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserProfileBasicDto {
    private Long UserProfileId;
    private String UserKoreaName;
    private Long PositionId1;
    private Long PositionId2;
    private com.PuzzleU.Server.common.enumSet.WorkType WorkType;
    private String UserRepresentativeProfileSentence;
}
