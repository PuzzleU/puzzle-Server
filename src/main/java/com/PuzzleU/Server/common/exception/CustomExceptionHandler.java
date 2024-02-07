package com.PuzzleU.Server.common.exception;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ErrorResponse;
import com.PuzzleU.Server.common.api.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    // 메서드에 전달된 인자의 유효성 검사에서 실패할 때 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // ErrorResponse를 사용하여 클라이언트에게 응답을 보낸다
    public ResponseEntity<ApiResponseDto<ErrorResponse>> methodValidException(MethodArgumentNotValidException e)
    {
        ErrorResponse response = ErrorResponse.of(e.getBindingResult());
        log.error(response.getMessage());
        // ResponseEntity를 사용하여 HTTP 응답을 구성하고 RsponseUtils.error(response)를 사용한다
        return ResponseEntity.badRequest().body(ResponseUtils.error(response));
    }
    // 런타임이 생겼을 때의 error
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> customException(RestApiException e)
    {
        ErrorResponse response = ErrorResponse.of(e.getErrorType());
        log.error(response.getMessage());
        return ResponseEntity.status(response.getStatus()).body(ResponseUtils.error(response));
    }
}
