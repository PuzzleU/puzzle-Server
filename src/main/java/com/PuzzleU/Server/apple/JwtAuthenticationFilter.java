package com.PuzzleU.Server.apple;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.PuzzleU.Server.apple.JwtValidationType.VALID_JWT;
import static io.jsonwebtoken.lang.Strings.hasText;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_HEADER = "Bearer ";
    private static final String BLANK = "";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 엑세스 토큰을 추출해서 토큰이 null이 아니고 유효하다면 사용자의 식별자를 추출한 후, 이를 기반으로 한 UserAuthentication객체를 생성하고, 보안 컨텍스트에 설정하여 애플리케이션 내의 다른 부분에서 현재 인증된 사용자에 대한 정보를 사용할 수 있도록 한다
        try {
            val token = getAccessTokenFromRequest(request);
            if (hasText(token) && jwtTokenProvider.validateToken(token) == VALID_JWT) {
                val authentication = new UserAuthentication(getMemberId(token), null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }
    // 요청에서 액세스 토큰을 추출해주는 getAccessTokenFromRequest
    private String getAccessTokenFromRequest(HttpServletRequest request) {
        return isContainsAccessToken(request) ? getAuthorizationAccessToken(request) : null;
    }
    // accessToken이 있는지
    private boolean isContainsAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        return authorization != null && authorization.startsWith(BEARER_HEADER);
    }

    // AuthorizationAccess토큰 추출
    private String getAuthorizationAccessToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION).replaceFirst(BEARER_HEADER, BLANK);
    }
    private long getMemberId(String token) {
        return jwtTokenProvider.getUserFromJwt(token);
    }


}