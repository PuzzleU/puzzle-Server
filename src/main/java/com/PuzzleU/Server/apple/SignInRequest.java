package com.PuzzleU.Server.apple;

import com.PuzzleU.Server.common.enumSet.LoginType;
import lombok.NonNull;

public record SignInRequest(
        @NonNull LoginType socialType
) {

    public static SignInRequest of(LoginType socialType) {
        return new SignInRequest(socialType);
    }
}