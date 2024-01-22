package com.PuzzleU.Server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "PuzzleU API Description",
                description = "PuzzleU API Description",
                version = "v0.0.1"))
@SecurityScheme(
        name = "Bearer Authentication", // 보안 스키마 이름 지정
        type = SecuritySchemeType.HTTP, // 보안 스키마 타입 지정
        bearerFormat = "JWT", // 토큰의 형식 지정
        scheme = "bearer" // 사용 인증 스키마 지정
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("PuzzleU MVP")
                .pathsToMatch(paths)
                .build();
    }
}
