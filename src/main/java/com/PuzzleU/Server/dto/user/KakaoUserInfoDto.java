package com.PuzzleU.Server.dto.user;

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
