package com.PuzzleU.Server.common.jwt;

import com.PuzzleU.Server.common.enumSet.UserRoleEnum;
import com.PuzzleU.Server.common.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // username 관련하여 사용하기 위해 userdetailsservice 받아옴
    private final UserDetailsServiceImpl userDetailsService;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";

    private static final String BEARER_PREFIX = "Bearer ";

    // 60 더 곱했습니다
    private static final long TOKEN_TIME = 60*60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct // spring 빈이 생성된 후에 자동으로 호출되는 초기화 메서드
    public void init()
    {
        // 시크릿 키를 base64로 디코딩하여서 byte배열로 변환한다
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // 위의 바이트를 사용하여 알고리즘을 통해 시크릿 키를 해시 키로 변환한다.
        key = Keys.hmacShaKeyFor(bytes);
    }

    // HTTP 요청에서 JWT 토큰을 추출 - header 토큰을 가져오기

    public String resolveToken(HttpServletRequest request)
    {
        // Authorization 헤더의 값을 가져온다 - 클라이언트가 서버에게 토큰을 전달하기 위해 사용되는 문자열이 포함
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // bearerToken이 null이 아니고 비어있는지 확인, 헤더에 Authorization키가 있고 그값이 비어 있지 않은지를 체크

//        System.out.println("resolveToken token: " + bearerToken);
        if (StringUtils.hasText(bearerToken)&& bearerToken.startsWith(BEARER_PREFIX))
        {
            // Bearer의 길이가 7이므로 7번째 문자부터 끝까지를 반환
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        // 형식이 맞지 않으면 null을 반환
        return null;
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role)
    {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder() // jwt를 생성하기 위한 빌더 객체
                        .setSubject(username) // 토큰의 주체를 설정
                        .claim(AUTHORIZATION_KEY,role) // 토큰에 사용자의 권한을 추가(Claim)
                        .setExpiration(new Date(date.getTime()+ TOKEN_TIME)) // 토큰 만료시간을 성정
                        .setIssuedAt(date) // 토큰의 발급 시간을 설정
                        .signWith(key, signatureAlgorithm) // 토큰에 서명을 추가, jwt서명키 + 서명 알고리즘
                        .compact(); // 최종적 jwt문자열을 생성
    }

    // 토큰 검증

    public boolean validateToken(String token)
    {
//        System.out.println("validate token: " + token);
        try{
            // jwt파싱을 위한 객체 생성 / 토큰의 서명 검증을 위해 사용할 키 설정 / 파서 생성 / 토큰을 파싱하고 클레임을 가져옴
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e)
        {
            log.info("Invalid JWT signature, 유효하지 않은 JWT 서명");
        }catch (ExpiredJwtException e){
            log.info("Expired JWT token, 만료된 JWT token 입니다");
        }catch (UnsupportedJwtException e)
        {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        }catch (IllegalArgumentException e)
        {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기 (클레임에 존재)
    public Claims getUserInfoFromToken(String token)
    {
        String cleanToken = token.replace(BEARER_PREFIX, "");
//        System.out.println("getUserInfoFromToken token: " + cleanToken);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(cleanToken).getBody();
        
//        // jwt 파싱하기 위한 빌더 객체 생성 / 토큰의 서명 검증을 위해 사용할 키 설정 / 파싱 생성 / 토큰을 파싱하고 클레임을 가져옴 / 토큰의 본문을 반환
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 사용자의 인증 정보를 가져온다
    public Authentication createAuthentication(String username)
    {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 사용자 정보 / 자격증명 / 권환
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
