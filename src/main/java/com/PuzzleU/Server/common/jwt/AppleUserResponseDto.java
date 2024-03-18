package com.PuzzleU.Server.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AppleUserResponseDto {
    private Long id;
    private String username;
    private String email;
}
