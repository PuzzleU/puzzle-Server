package com.PuzzleU.Server.common.jwt;

import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String message;
    private String accessToken;
    private String refreshToken;
}
