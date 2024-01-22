package com.PuzzleU.Server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {
    @Bean
    // RestTemplate은 HTTP 통신 자체를 담당하는 라이브러리고
    // 기존에 사용하던 ResponseEntity는 그 통신의 결과를 클래스
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
