package com.PuzzleU.Server.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KakaoUserInfoDto {
    private String kakaoId;
    private String nickname;
}
