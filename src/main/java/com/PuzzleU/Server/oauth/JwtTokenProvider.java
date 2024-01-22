package com.PuzzleU.Server.oauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
// 제작 6번
/**
 * JSON Web Token (JWT)을 생성하고 해석하는 역할을 담당하는 컴포넌트.
 */
@Component
public class JwtTokenProvider {

    // JWT 서명 알고리즘 및 시크릿 키
    private final Key key;

    /**
     * JwtTokenProvider 생성자는 주어진 시크릿 키를 사용하여 Key 객체를 초기화.
     *
     * @param secretKey 시크릿 키
     */
    public JwtTokenProvider(
            @Value("${jwt.secret.key}")
            String secretKey) {
        // Base64 디코딩된 시크릿 키를 이용하여 HMAC SHA 키 생성
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 주어진 subject와 만료 시간을 사용하여 JWT를 생성.
     *
     * @param subject   JWT subject (사용자 식별 정보)
     * @param expiredAt JWT 만료 시간
     * @return 생성된 JWT
     */
    public String generate(String subject, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 주어진 Access Token에서 subject를 추출하여 반환.
     *
     * @param accessToken 추출할 Access Token
     * @return 추출된 subject (사용자 식별 정보)
     */
    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    /**
     * 주어진 Access Token을 해석하여 그 안에 포함된 Claims를 반환.
     * 만료된 토큰의 경우 ExpiredJwtException이 발생하며 해당 Claims를 반환.
     *
     * @param accessToken 해석할 Access Token
     * @return 해석된 Claims
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}