package com.PuzzleU.Server.common;

import com.PuzzleU.Server.entity.enumSet.ErrorType;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class ErrorResponse {
    private int status;
    private String message;

    @Builder
    private ErrorResponse(int status, String message)
    {
        this.status = status;
        this.message = message;
    }

    // 에러 타입에 대해서 에러 타입에서 어떤 코드고 어떤 메시지를 담는지 각각 가져와서 넣어주고 생성
    public static ErrorResponse of(ErrorType errorType)
    {
        return ErrorResponse.builder()
                .status(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }
    // http 상 에러가 생길 때 http에러와 메시지를 담아서 errorresponse 생성
    public static ErrorResponse of(HttpStatus status, String message)
    {
        return ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .build();
    }
    // BindingResult(Spring MVC에서 양식 제출의 바인딩 결과를 나타내고 데이터 유효성 검사를 수행)
    public static ErrorResponse of(BindingResult bindingResult) {
        String message = "";
        // 유효성 검사를 한다
        if (bindingResult.hasErrors()) {
            // 첫 번째 오류의 기본 오류 메시지를 가져온다
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        // HttpStatus.BAD_REQUEST 상태와 오류 메시지로 ErrorResponse 객체를 생성하고 반환
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
    }


}
