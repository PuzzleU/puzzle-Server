
package com.PuzzleU.Server.service.Impl;
import com.PuzzleU.Server.common.ApiResponseDto;
import com.PuzzleU.Server.common.ResponseUtils;
import com.PuzzleU.Server.common.SuccessResponse;
import com.PuzzleU.Server.common.TokenDecoder;
import com.PuzzleU.Server.entity.apple.AppleIdTokenPayload;
import com.PuzzleU.Server.entity.apple.AppleProperties;
import com.PuzzleU.Server.entity.enumSet.UserRoleEnum;
import com.PuzzleU.Server.entity.user.User;
import com.PuzzleU.Server.jwt.JwtUtil;
import com.PuzzleU.Server.repository.UserRepository;
import com.PuzzleU.Server.service.AppleAuthClient;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AppleService {

    // 인가코드를 받으면
    // appleidtokenpayload로 client secret을 생성
    // clinet_secret과 team_id로 애플 토큰 검증 api(POST https://appleid.apple.com/auth/token)검증
    // access_token과 id_token을 반환
    // id_token이 jwt형식이므로 해당 토큰 파싱
    // 애플 고유 아이디, 이메일 저장
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppleAuthClient appleAuthClient;
    private final AppleProperties appleProperties;
    private final JwtUtil jwtUtil;




    //
    // Authorization Code를 사용하여 ID Token을 얻어와서 디코딩하여 AppleIdTokenPayload 객체로 변환
    // @param authorizationCode
    // @return

    public AppleIdTokenPayload get(String authorizationCode)
    {
        // id 토큰을 얻고
        String idToken = appleAuthClient.getIdToken(
                // 클라이언트id를 얻고
                appleProperties.getClientId(),
                // secret을 만들고
                generateClientSecret(),
                // OAuth2.0인증 코드 부여하고
                appleProperties.getGrantType(),
                // code 넣고
                authorizationCode

                ).getIdToken();
        // 얻은 ID를 디코딩하여 AppleIdTokenPayload 객체로 변환
        // apple인증 서번의 id 토큰, 디코딩 정보를 담을 객체의 클래스 타입
        return TokenDecoder.decodePayLoad(idToken, AppleIdTokenPayload.class);
    }


    // * Apple의 클라이언트 시크릿을 생성하는 메서드
    // *
    // * @return

    public String generateClientSecret()
    {
        // 클라이언트 시크릿 만료기간 5분
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
        return Jwts.builder()
                // 헤더에 apple 키를 넣는다
                .setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
                // 발급자(issuer)는 apple개발자 계정의 팀 식별자
                .setIssuer(appleProperties.getTeamId())
                // jwt의 대상 청중(aduidence)설정 -> apple의 인증 서버의 url
                .setAudience(appleProperties.getAudience())
                // jwt의 주제(subject) 클라이언트 ID
                .setSubject(appleProperties.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivatekey(), SignatureAlgorithm.ES256)
                // JWT를 문자열로 반환
                .compact();
    }

    // base64로 인코딩된 문자열로 제공된 개인 키('appleProperties.getPrivateKey())를 PrivateKey 객체로 변환
    // 주로 암호화 및 서명 관련 작업에서 개인키를 사용한다
    // Apple 서비스와 통신하거나 암호화 및 서명에서 개인키를 준비하는데 구현되는 것
    //
    public PrivateKey getPrivatekey()
    {
        // 암호화 및 보안 관련 작업을 지원하는 라이브러리 boundcycastle제공자를 추가
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        // JcaPEMKeyConverter 생성 후, PEM 형식의 키를 자바 객체로 변환
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try{
            // KEY 문자열을 디코딩하여 바이트 배열로 변환
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());
            // 객체로 개인 키를 파싱한다.
            PrivateKeyInfo privateKeyInfo= PrivateKeyInfo.getInstance(privateKeyBytes);
            // JcaPEMKeyConverter를 사용하여 PricateKeyInfo 객체를 PricateKey로 변환하여 반환
            return converter.getPrivateKey(privateKeyInfo);
        } catch(Exception e)
        {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }
    public ApiResponseDto<SuccessResponse> appleLogin(String code)
    {

        AppleIdTokenPayload appleIdTokenPayload = get(code);
        String username = appleIdTokenPayload.getSub();
        String password = passwordEncoder.encode("appleuserpassword");
        // 회원 아이디 중복 확인 -> DB에 존재하지 않으면 회원가입 수행
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            // 입력한 username, password, admin 으로 user 객체 만들어 repository 에 저장
            UserRoleEnum role = UserRoleEnum.USER; // 카카오 유저 ROLE 임의 설정
            userRepository.save(User.of(username, password, role));
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"), null);
        } else { // DB에 존재하면 로그인 수행
            String jwtToken = jwtUtil.createToken(user.get().getUsername(), user.get().getRole());
            jwtToken.substring(7);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "로그인 성공"),jwtToken);
        }

    }
}
