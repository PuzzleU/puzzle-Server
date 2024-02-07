package com.PuzzleU.Server.common.exception;

import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AppleFeignClientErrorDecoder implements ErrorDecoder {

    // JSON응답을 객체로 매핑하기 위해 사용
    private final ObjectMapper objectMapper;

    //
    //  애플 로그린 Feign API 연동 시 발생되는 오류 예외 처리
    // @param methodKey Feign Client 메서드 이름
    // @param response 응답정보
    // @return 예외를 리턴
     //
    @Override
    public Exception decode(String methodKey, Response response) { //Feign 클라이언트 메서드 이름, 오류 응답 정보
        Object body = null;
        if(response != null && response.body() != null)
        {
            try
            {
                // body에 있는 json을 객체로 변환한다
                body = objectMapper.readValue(response.body().toString(), Object.class);
            } catch (IOException e)
            {
                log.error("Error decoding response body", e);
            }
        }
        log.error("애플 로그인 Feign API Client 호출 중 오류 발생. body : {}", body);


        return new RestApiException(ErrorType.NOT_VALID_CLIENT);
    }
}
