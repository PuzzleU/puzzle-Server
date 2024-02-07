package com.PuzzleU.Server.dto.user;

import com.PuzzleU.Server.entity.enumSet.WorkType;
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
    private WorkType WorkType;
    private String UserRepresentativeProfileSentence;
}
