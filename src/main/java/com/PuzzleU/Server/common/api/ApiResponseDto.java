package com.PuzzleU.Server.common.api;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponseDto<T>  {

    private boolean success;
    private T response;
    private ErrorResponse error;


    @Builder
    ApiResponseDto(boolean success, T response, ErrorResponse error, String jwt)
    {
        this.success = success;
        this.response = response;
        this.error = error;
    }
}
