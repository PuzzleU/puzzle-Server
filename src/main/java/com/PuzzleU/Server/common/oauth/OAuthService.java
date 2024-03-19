package com.PuzzleU.Server.common.oauth;

import com.PuzzleU.Server.common.jwt.AppleUserInfoDto;
import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.enumSet.LoginType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.common.jwt.TokenDto;
import com.PuzzleU.Server.user.dto.KakaoUserInfoDto;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.common.enumSet.UserRoleEnum;
import com.PuzzleU.Server.common.jwt.JwtUtil;
import com.PuzzleU.Server.user.repository.UserRepository;
import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /*
        카카오 로그인
     */

    // code를 넘기면 access token을 반환
    public String getKakaoAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token"; // access token을 반환해주는 주소 (카카오에서 제공하는 기능)

        try {
            URL url = new URL(reqURL); // Url 처리
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 실제 서버와의 연결을 설정 + 반환된 객체를 HttpURLConnection으로 캐스팅

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로 설정
            conn.setRequestMethod("POST");
            conn.setDoOutput(true); // 객체에서 출력 스트림을 사용할 것인지를 설정하는 메서드 -> 서버로 데이터를 보낼 때 사용

            // POST 요청에 필요로 요구하는 파라미터를 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(conn.getOutputStream()))); // 전송하기 위한 것
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=bdae78483f052375d4334586ceee5544");
            sb.append("&redirect_uri=http://localhost:8080/api/oauth/kakao");
            sb.append("&client_secret=oxOzQ4KuQ4vBJqIZTteJ2nf21RgBZRgA"); // 이거는 나중에 코드에서 삭제해야 함(secret key니까!)
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            String result = new String();

            if (responseCode >= 200 && responseCode < 300) {
                // 성공적인 응답
                // 서버가 응답한 데이터를 읽어오기
                InputStream inputStream = conn.getInputStream();
                // InputStream을 문자열로 변환
                result = new BufferedReader(new InputStreamReader(inputStream))
                        // 스트림에서 읽은 각 라인들을 개행문자열로 연결하여 하나의 문자열로 만든다
                        .lines().collect(Collectors.joining("\n"));

                System.out.println("result = " + result);
            } else {
                // 에러 응답
                InputStream errorStream = conn.getErrorStream();
                // 에러 스트림을 문자열로 변환
                String errorResult = new BufferedReader(new InputStreamReader(errorStream))
                        .lines().collect(Collectors.joining("\n"));

                System.out.println("Error result = " + errorResult);
            }

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(refreshToken);
        return accessToken;
    }

    // access token을 넘기면 kakao user 정보를 반환
    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        KakaoUserInfoDto kakaoUserInfoDto = new KakaoUserInfoDto();

        // access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            String result = new String();

            if (responseCode >= 200 && responseCode < 300) {
                // 성공적인 응답
                InputStream inputStream = conn.getInputStream();
                // InputStream을 문자열로 변환
                result = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));

                System.out.println("result = " + result);
            } else {
                // 에러 응답
                InputStream errorStream = conn.getErrorStream();
                // 에러 스트림을 문자열로 변환
                String errorResult = new BufferedReader(new InputStreamReader(errorStream))
                        .lines().collect(Collectors.joining("\n"));

                System.out.println("Error result = " + errorResult);
            }

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonElement kakaoAccount = element.getAsJsonObject().get("kakao_account");
            JsonElement profile = kakaoAccount.getAsJsonObject().get("profile");

            //dto에 저장하기
            kakaoUserInfoDto.setKakaoId(element.getAsJsonObject().get("id").getAsString());
            kakaoUserInfoDto.setNickname(profile.getAsJsonObject().get("nickname").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kakaoUserInfoDto;
    }

    // 카카오 유저정보 받아오기
    public KakaoUserInfoDto kakaoUserDetail(String code) {

        String accessToken = getKakaoAccessToken(code); // 카카오 access token 받아오기
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken); // 카카오 유저 정보 받아오기

        return kakaoUserInfoDto;
    }

//    @Transactional
//    // 기존 카카오 로그인 매서드
//    public ApiResponseDto<TokenDto> kakaoLogin(String code) {
//        /*
//            *** 아래 링크에서 카카오 로그인 하면 회원가입/로그인 됨 ***
//            kauth.kakao.com/oauth/authorize?client_id=bdae78483f052375d4334586ceee5544&redirect_uri=http://localhost:8080/api/oauth/kakao&response_type=code
//
//            1. kauth.kakao.com/oauth/authorize로 들어가서 카카오 로그인 수행 -> 로그인 코드 반환
//            2. kapi.kakao.com/v2/user/me에서 로그인 코드 입력 -> 엑세스 토큰 반환
//            3. 엑세스 토큰으로 유저 정보 받아오기
//            4-1. DB에 없는 ID면 -> 회원가입
//            4-2. DB에 있는 ID면 -> 로그인
//         */
//
//        String kakaoAccessToken = getKakaoAccessToken(code); // 카카오 access token 받아오기
//        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(kakaoAccessToken); // 카카오 유저 정보 받아오기
//
//        // 아이디, 비밀번호 받아오기
//        String username = kakaoUserInfoDto.getKakaoId();
//        String password = passwordEncoder.encode("kakaouserpassword"); // 카카오 유저 비밀번호 임의 설정
//
//        // 회원 아이디 중복 확인 -> DB에 존재하지 않으면 회원가입 수행
//        Optional<User> user = userRepository.findByUsername(username);
//
//        if (user.isEmpty()) {
//            // 입력한 username, password, admin 으로 user 객체 만들어 repository 에 저장
//            UserRoleEnum role = UserRoleEnum.USER; // 카카오 유저 ROLE 임의 설정
//            User signUpUser = User.of(username, password, role);
//
//            // 토큰 생성
//            TokenDto tokenDto = new TokenDto();
//
//            String accessToken = jwtUtil.createAccessToken(signUpUser.getUsername(), signUpUser.getRole());
//            String refreshToken = jwtUtil.createRefreshToken(signUpUser.getUsername(), signUpUser.getRole());
//
//            // refresh token을 DB에 저장
//            signUpUser.setKakaoRefreshToken(refreshToken);
//            userRepository.save(signUpUser);
//
//            // response 생성
//            tokenDto.setMessage("카카오 회원가입 성공");
//            tokenDto.setAccessToken(accessToken);
//            tokenDto.setRefreshToken(refreshToken);
//
//            return ResponseUtils.ok(tokenDto, null);
//
//        } else { // DB에 존재하면 로그인 수행
//            // 토큰 생성
//            TokenDto tokenDto = new TokenDto();
//
//            String accessToken = jwtUtil.createAccessToken(user.get().getUsername(), user.get().getRole());
//            String refreshToken = jwtUtil.createRefreshToken(user.get().getUsername(), user.get().getRole());
//
//            // refresh token을 DB에 저장
//            user.get().setKakaoRefreshToken(refreshToken);
//            userRepository.save(user.get());
//
//            // response 생성
//            tokenDto.setMessage("카카오 로그인 성공");
//            tokenDto.setAccessToken(accessToken);
//            tokenDto.setRefreshToken(refreshToken);
//
//            return ResponseUtils.ok(tokenDto, null);
//        }
//    }

        // 카카오 로그인 - 엑세스 토큰 받아서 로그인
        @Transactional
    public ApiResponseDto<TokenDto> kakaoLogin(String kakaoAccesstoken) {
        /*
            1. 카카오 엑세스 토큰으로 유저 정보 받아오기
            2-1. DB에 없는 ID면 -> 회원가입
            2-2. DB에 있는 ID면 -> 로그인
         */

        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(kakaoAccesstoken); // 카카오 유저 정보 받아오기

        // 아이디, 비밀번호 받아오기
        String username = kakaoUserInfoDto.getKakaoId();
        String password = passwordEncoder.encode("kakaouserpassword"); // 카카오 유저 비밀번호 임의 설정

        // 회원 아이디 중복 확인 -> DB에 존재하지 않으면 회원가입 수행
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            // 입력한 username, password, admin 으로 user 객체 만들어 repository 에 저장
            UserRoleEnum role = UserRoleEnum.USER; // 카카오 유저 ROLE 임의 설정
            User signUpUser = User.of(username, password, role);
            signUpUser.setLoginType(LoginType.KAKAO);

            // 토큰 생성
            TokenDto tokenDto = new TokenDto();

            String accessToken = jwtUtil.createAccessToken(signUpUser.getUsername(), signUpUser.getRole());
            String refreshToken = jwtUtil.createRefreshToken(signUpUser.getUsername(), signUpUser.getRole());

            // refresh token을 DB에 저장
            signUpUser.setKakaoRefreshToken(refreshToken);
            userRepository.save(signUpUser);

            // response 생성
            tokenDto.setMessage("카카오 회원가입 성공");
            tokenDto.setAccessToken(accessToken);
            tokenDto.setRefreshToken(refreshToken);

            return ResponseUtils.ok(tokenDto, null);

        } else { // DB에 존재하면 로그인 수행
            // 토큰 생성
            TokenDto tokenDto = new TokenDto();

            String accessToken = jwtUtil.createAccessToken(user.get().getUsername(), user.get().getRole());
            String refreshToken = jwtUtil.createRefreshToken(user.get().getUsername(), user.get().getRole());

            // refresh token을 DB에 저장
            user.get().setKakaoRefreshToken(refreshToken);
            userRepository.save(user.get());

            // response 생성
            tokenDto.setMessage("카카오 로그인 성공");
            tokenDto.setAccessToken(accessToken);
            tokenDto.setRefreshToken(refreshToken);

            return ResponseUtils.ok(tokenDto, null);
        }
    }

    // refresh token으로 access token 재발급
    @Transactional
    public ApiResponseDto<TokenDto> refreshKakaoToken(String accessTokenOrigin, String refreshTokenOrigin) {

        String accessToken = accessTokenOrigin.substring(7);
        String refreshToken = refreshTokenOrigin.substring(7);

        // 아직 만료되지 않은 토큰으로는 refresh 할 수 없음
        if(jwtUtil.validateToken(accessToken)) throw new RestApiException(ErrorType.ACCESS_TOKEN_NOT_EXPIRED);

        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        String username = claims.get("sub", String.class); // access token에서 username을 가져옴

        System.out.println(username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        String kakaoRefreshToken = user.getKakaoRefreshToken();

        if (!jwtUtil.validateToken(kakaoRefreshToken.substring(7)) || !refreshToken.equals(kakaoRefreshToken.substring(7))) {
            throw new RestApiException(ErrorType.REFRESH_TOKEN_NOT_VALIDATE); // 만료되거나 일치하지 않는 리프레시 토큰은 에러처리
        }

        String newAccessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());

        TokenDto tokenDto = new TokenDto();
        tokenDto.setMessage("access token 재발급 성공");
        tokenDto.setAccessToken(newAccessToken);
        tokenDto.setRefreshToken(refreshTokenOrigin);

        return ResponseUtils.ok(tokenDto, null);
    }

    @Transactional
    public ApiResponseDto<TokenDto> appleLogin(String appleAccessToken) {
        /*
            1. 애플 엑세스 토큰으로 유저 정보 받아오기
            2-1. DB에 없는 ID면 -> 회원가입
            2-2. DB에 있는 ID면 -> 로그인
         */
        AppleUserInfoDto appleUserInfoDto = getAppleUserInfo(appleAccessToken);
        // access토큰에서 각각의 정보를 받아와야한다
        // 아이디, 비밀번호 받아오기
        String username = appleUserInfoDto.getUsername();
        String password = passwordEncoder.encode("appleuserpassword"); // 애플 유저 비밀번호 임의 설정

        // 회원 아이디 중복 확인 -> DB에 존재하지 않으면 회원가입 수행
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            // 입력한 username, password, admin 으로 user 객체 만들어 repository 에 저장
            UserRoleEnum role = UserRoleEnum.USER; // 카카오 유저 ROLE 임의 설정
            User signUpUser = User.of(LoginType.APPLE, username, password, role);
            signUpUser.setLoginType(LoginType.APPLE);

            // 토큰 생성
            TokenDto tokenDto = new TokenDto();

            String accessToken = jwtUtil.createAccessToken(signUpUser.getUsername(), signUpUser.getRole());
            String refreshToken = jwtUtil.createRefreshToken(signUpUser.getUsername(), signUpUser.getRole());

            // refresh token을 DB에 저장
            signUpUser.setAppleRefreshToken(refreshToken);
            userRepository.save(signUpUser);

            // response 생성
            tokenDto.setMessage("애플 회원가입 성공");
            tokenDto.setAccessToken(accessToken);
            tokenDto.setRefreshToken(refreshToken);

            return ResponseUtils.ok(tokenDto, null);

        } else { // DB에 존재하면 로그인 수행
            // 토큰 생성
            TokenDto tokenDto = new TokenDto();

            String accessToken = jwtUtil.createAccessToken(user.get().getUsername(), user.get().getRole());
            String refreshToken = jwtUtil.createRefreshToken(user.get().getUsername(), user.get().getRole());

            // refresh token을 DB에 저장
            user.get().setAppleRefreshToken(refreshToken);
            userRepository.save(user.get());

            // response 생성
            tokenDto.setMessage("애플 로그인 성공");
            tokenDto.setAccessToken(accessToken);
            tokenDto.setRefreshToken(refreshToken);

            return ResponseUtils.ok(tokenDto, null);
        }
    }
    @Transactional
    public ApiResponseDto<TokenDto> refreshAppleToken(String accessTokenOrigin, String refreshTokenOrigin) {

        String accessToken = accessTokenOrigin.substring(7);
        String refreshToken = refreshTokenOrigin.substring(7);

        // 아직 만료되지 않은 토큰으로는 refresh 할 수 없음
        if(jwtUtil.validateToken(accessToken)) throw new RestApiException(ErrorType.ACCESS_TOKEN_NOT_EXPIRED);

        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        String username = claims.get("sub", String.class); // access token에서 username을 가져옴

        System.out.println(username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));

        String kakaoAppleToken = user.getAppleRefreshToken();

        if (!jwtUtil.validateToken(kakaoAppleToken.substring(7)) || !refreshToken.equals(kakaoAppleToken.substring(7))) {
            throw new RestApiException(ErrorType.REFRESH_TOKEN_NOT_VALIDATE); // 만료되거나 일치하지 않는 리프레시 토큰은 에러처리
        }

        String newAccessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());

        TokenDto tokenDto = new TokenDto();
        tokenDto.setMessage("access token 재발급 성공");
        tokenDto.setAccessToken(newAccessToken);
        tokenDto.setRefreshToken(refreshTokenOrigin);

        return ResponseUtils.ok(tokenDto, null);
    }
    /**
     * 1. apple로 부터 공개키 3개 가져옴
     * 2. 내가 클라에서 가져온 token String과 비교해서 써야할 공개키 확인 (kid, alg값 확인)
     * 3. 그 공캐기 재료들로 공개키 만들고, 이 공개키로 JWT 토큰 부분의 바디 부분의 decode하면 유저 정보
     */
    public AppleUserInfoDto getAppleUserInfo(String idToken)
    {
        StringBuffer result = new StringBuffer();
        AppleUserInfoDto appleUserInfoDto = new AppleUserInfoDto();
        try{
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = br.readLine())!=null)
            {
                result.append(line);
            }
        }catch (IOException e)
        {
            return null;
        }

        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray keyArray = (JsonArray) keys.get("keys");

        // 클라이언트로부터 가져온 identity token String decode
        String[] decodeArray = idToken.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        //apple에서 제공해주는 kid값과 일치하는지 알기 위해
        JsonElement kid = ((JsonObject) parser.parse(header)).get("kid");
        JsonElement alg = ((JsonObject) parser.parse(header)).get("alg");

        // 서야하는 Element (kid, alg 일치하는 element)
        JsonObject availableOnject = null;
        for (int i=0; i< keyArray.size(); i++)
        {
            JsonObject appleObject = (JsonObject) keyArray.get(i);
            JsonElement appleKid = appleObject.get("kid");
            JsonElement appleAlg = appleObject.get("alg");

            if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg))
            {
                availableOnject = appleObject;
                break;
            }
        }
        // 일치하는 공개키 없음
        if(ObjectUtils.isEmpty(availableOnject))
            return null;

        PublicKey publicKey = this.getPublicKey(availableOnject);
        Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(idToken).getBody();
        JsonObject userInfoObject = (JsonObject) parser.parse(new Gson().toJson(userInfo));
        System.out.println("userInfoObject = " + userInfoObject);
        JsonObject userData = userInfoObject.getAsJsonObject().get("user").getAsJsonObject();
        String socialId = userData.getAsJsonObject("socialId").getAsString();
        String email = userData.getAsJsonObject("email").getAsString();
        String username = userData.getAsJsonObject("fullname").getAsString();

        appleUserInfoDto.setUsername(username);
        appleUserInfoDto.setEmail(email);
        appleUserInfoDto.setId(socialId);
        appleUserInfoDto.setLoginType(LoginType.APPLE);

        return appleUserInfoDto;

    }

    public PublicKey getPublicKey(JsonObject object) {
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            return null;
        }
    }
}
