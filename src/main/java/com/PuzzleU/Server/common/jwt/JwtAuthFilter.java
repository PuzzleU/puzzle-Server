package com.PuzzleU.Server.common.jwt;

import com.PuzzleU.Server.common.enumSet.ErrorType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;

    @Override
    // HTTP 요청이 올 때마다 실행되며 주로 JWT 토큰을 사용하여 사용잦를 인증하고 권한을 부여
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에 담긴 토큰을 가져온다
        // HTTP 요청으로 부터 JWT 토큰을 추출한다
        String token = jwtUtil.resolveToken(request);

        // 토큰이 null이거나 인증되지 않으면 다음 필터로 넘어간다
        if (token == null || !jwtUtil.validateToken(token)) {
            request.setAttribute("exception", ErrorType.NOT_VALID_TOKEN);
            filterChain.doFilter(request, response);
            return;

        }
        // 유효한 토큰이라면, 토큰으로 부터 (클레임)사용자 정보를 가져온다.

        Claims info = jwtUtil.getUserInfoFromToken(token);
        try {
            setAuthentication(info.getSubject()); //  사용자명을 반환 받아서 사용자 정보로 인증 객체 만들기 -> 현재의 SecurityContext에 설정한다
        } catch (UsernameNotFoundException e) {
            request.setAttribute("exception", ErrorType.NOT_FOUND_USER);
        }
        // 다음 필터로 넘어간다
        filterChain.doFilter(request, response);
    }

    // jwt가 유효할 시 username을 사용하여서 Spring Security의 Authentication 객체를 생성하고 SecurityContext에 설정
    private void setAuthentication(String username) {

        SecurityContext context = SecurityContextHolder.createEmptyContext(); // 비어있는 SecurityCotext객체를 생성
        Authentication authentication = jwtUtil.createAuthentication(username); // 주어진 사용자명으로 부터 UserDetails를 사져와서 UsernamePasswordAuthenticationToken 객체를 생성하여 반환한다
        ; // 인증 객체 만들기
        context.setAuthentication(authentication);
        // securitycontext에 authentication객체를 설정
        SecurityContextHolder.setContext(context);
        // 최정적으로 securitycontextholder에 securityconetext를 설정
    }

}
