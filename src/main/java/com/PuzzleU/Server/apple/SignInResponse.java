package com.PuzzleU.Server.apple;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SignInResponse(
        @NonNull String accessToken,
        @NonNull String refreshToken
) {

    public static SignInResponse of(Token token, boolean isMemberDollExist) {
        return SignInResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}