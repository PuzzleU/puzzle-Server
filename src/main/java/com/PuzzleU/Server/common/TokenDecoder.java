package com.PuzzleU.Server.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class TokenDecoder {

    public static <T> T decodePayLoad(String token, Class<T> targetClass)
    {
        // token을 \\.을 기준으로 3부분 (헤더, 페이로드, 서명)으로 나눈다
        String[] tokenParts = token.split("\\.");
        String payloadJWT = tokenParts[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        // 페이로드를 base64로 디코딩한다. 문자열로 변환
        String payload = new String(decoder.decode(payloadJWT));
        ObjectMapper objectMapper = new ObjectMapper()
                // false로 지정하여 JSON에 명시되지 않은 속성이 있어도 예외를 발생시키지 않도록 한다
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // payload를 targetClass로 변환한다. readValue는 JSON을 객체로 변환
            return objectMapper.readValue(payload, targetClass);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding token payload", e);
        }
    }
}
