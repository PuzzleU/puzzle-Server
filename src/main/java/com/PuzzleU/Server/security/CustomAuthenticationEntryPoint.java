package com.PuzzleU.Server.security;

import com.PuzzleU.Server.common.ErrorResponse;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.entity.enumSet.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j // 자동 로깅을 위한 log 변수를 생성해준다
@Component
// springsecurity에서 인증 실패시 호출 되는 인터페이스(AuthenticationEntryPoint는 인증에 실패하거나 인증되지 않은 사용자의 요청에 대한 처리를 담당)
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    // 인증 예외가 발생했을 때 호출되는 메서드  (인증 실패, 미인증 요청)
    // 현재 요청에 대한 정보를 제공 / 응답을 제어하는데 사용 / 발생한 인증 예외에 대한 정보를 포함
    public void commence(HttpServletRequest request , HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // request에서 받은 exception속성을 ErrorType으로 캐스팅 한다
        ErrorType exception = (ErrorType) request.getAttribute("exception");

        // 각각의 상황에 따라 예외 처리를 한다
        if(exception.equals(ErrorType.NOT_VALID_TOKEN))
        {
            exceptionHandler(response, ErrorType.NOT_VALID_TOKEN);
            return;
        }
        if (exception.equals(ErrorType.NOT_FOUND_USER))
        {
            exceptionHandler(response, ErrorType.NOT_FOUND_USER);
        }
    }
    // 예외 발생시 클라이언트에게 json 형식으로 에러 응답을 보냄
    public void exceptionHandler(HttpServletResponse response, ErrorType error) {
        // Http응답 상태 코드를 설정 - errortype에서 가져온 에러코드에 해당하는 http상태 코드설정
        response.setStatus(error.getCode());
        // Http응답헤더에 Json 형식의 컨텐츠를 사용할 것임을 설정
        response.setContentType("application/json");
        //HTTP 응답의 문자 인코딩을 설정
        response.setCharacterEncoding("UTF-8");
        try {
            // ErrorResponse 객체(에러에 대한 상세 정보를 가지고 있는 클래스 - ErrorResponse를 거쳐 ResponseUtils로)를 JSON 문자열로 반환
            String json = new ObjectMapper().writeValueAsString(ResponseUtils.error(ErrorResponse.of(error)));
            // 응답을 작성하는 PrintWriter를 얻고 JSON문자열을 작성
            response.getWriter().write(json);
            log.error(error.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
