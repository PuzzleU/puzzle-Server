package com.PuzzleU.Server.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserRegisterEssentialDto {
    private String UserKoreaName;
    private String UserPuzzleId;
    private Long UserProfileId; // 우선 순위 1
    private Long UserPositionId1; // 우선 순위 2
    private Long UserPositionId2;
    private List<Long> UserInterestIdList;
    private List<Long> UserLocationIdList;
}
