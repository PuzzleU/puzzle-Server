package com.PuzzleU.Server.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.ErrorResponse;

@Getter
public class ApiResponseDto<T> {

    private boolean success;
    private T response;
    private ErrorResponse error;

    @Builder
    ApiResponseDto(boolean success, T response, ErrorResponse error)
    {
        this.success = success;
        this.response = response;
        this.error = error;
    }
}
