package com.PuzzleU.Server.common.oauth;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.user.dto.KakaoUserInfoDto;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.common.enumSet.UserRoleEnum;
import com.PuzzleU.Server.common.jwt.JwtUtil;
import com.PuzzleU.Server.user.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    // 카카오 로그인 매서드
    public ApiResponseDto<SuccessResponse> kakaoLogin(String code) {
        /*
            *** 아래 링크에서 카카오 로그인 하면 회원가입/로그인 됨 ***
            kauth.kakao.com/oauth/authorize?client_id=bdae78483f052375d4334586ceee5544&redirect_uri=http://localhost:8080/api/oauth/kakao&response_type=code

            1. kauth.kakao.com/oauth/authorize로 들어가서 카카오 로그인 수행 -> 로그인 코드 반환
            2. kapi.kakao.com/v2/user/me에서 로그인 코드 입력 -> 엑세스 토큰 반환
            3. 엑세스 토큰으로 유저 정보 받아오기
            4-1. DB에 없는 ID면 -> 회원가입
            4-2. DB에 있는 ID면 -> 로그인
         */

        String accessToken = getKakaoAccessToken(code); // 카카오 access token 받아오기
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken); // 카카오 유저 정보 받아오기

        // 아이디, 비밀번호 받아오기
        String username = kakaoUserInfoDto.getKakaoId();
        String password = passwordEncoder.encode("kakaouserpassword"); // 카카오 유저 비밀번호 임의 설정

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
