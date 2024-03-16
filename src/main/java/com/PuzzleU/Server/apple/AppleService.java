package com.PuzzleU.Server.apple;

import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Objects;

import static org.springframework.util.ResourceUtils.toURL;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppleService {

    // apple 공개키 가져오기
    private JsonArray getApplePublicKeys() {
        HttpURLConnection connection = sendHttpRequest();
        StringBuilder result = getHttpResponse(connection);
        JsonObject keys = (JsonObject) JsonParser.parseString(result.toString());
        return (JsonArray) keys.get("keys");
    }


    // 애플 서버에 http 요청을 보낸다
    private HttpURLConnection sendHttpRequest() {
        try {
            URL url = toURL("https://appleid.apple.com/auth/keys");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.GET.name());
            return connection;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    // http 응답을 얻어온다
    private StringBuilder getHttpResponse(HttpURLConnection connection) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return splitHttpResponse(bufferedReader);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    // 응답을 한 줄씩 잘라준다
    private StringBuilder splitHttpResponse(BufferedReader bufferedReader) {
        try {
            StringBuilder result = new StringBuilder();

            String line;
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                result.append(line);
            }
            bufferedReader.close();

            return result;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    private static final String TOKEN_VALUE_DELIMITER = "\\.";
    private static final String MODULUS = "n";
    private static final String EXPONENT = "e";
    private static final int QUOTES = 1;
    private static final int POSITIVE_NUMBER = 1;

    // socialAccessToken에서 Bearer를 뺀 나머지 부분을 디코딩 -> kid와 alg에 해당하는 부분을 추출한다
    private PublicKey makePublicKey(String accessToken, JsonArray publicKeyList) {
        String[] decodeArray = accessToken.split(TOKEN_VALUE_DELIMITER);
        String header = new String(Base64.getDecoder().decode(decodeArray[0].replaceFirst("Bearer ", "")));

        JsonElement kid = ((JsonObject) JsonParser.parseString(header)).get("kid");
        JsonElement alg = ((JsonObject) JsonParser.parseString(header)).get("alg");
        JsonObject matchingPublicKey = findMatchingPublicKey(publicKeyList, kid, alg);

        if (Objects.isNull(matchingPublicKey)) {
            throw new IllegalArgumentException();
        }

        return getPublicKey(matchingPublicKey);
    }
    // 뽑아온 공개 키 목록에서 일치하는 부분을 가져온다
    private JsonObject findMatchingPublicKey(JsonArray publicKeyList, JsonElement kid, JsonElement alg) {
        for (JsonElement publicKey : publicKeyList) {
            JsonObject publicKeyObject = publicKey.getAsJsonObject();
            JsonElement publicKid = publicKeyObject.get("kid");
            JsonElement publicAlg = publicKeyObject.get("alg");

            if (Objects.equals(kid, publicKid) && Objects.equals(alg, publicAlg)) {
                return publicKeyObject;
            }
        }

        return null;
    }

    // Json 객체에서 MODULUS("n")와 EXPONENT("e")키에 해당하는 값을 문자열로 추출한다 -> 인용부호 제거 후 디코딩 + RSA 키 객체 생성 + PublicKey생성
    private PublicKey getPublicKey(JsonObject object) {
        try {
            String modulus = object.get(MODULUS).toString();
            String exponent = object.get(EXPONENT).toString();

            byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus.substring(QUOTES, modulus.length() - QUOTES));
            byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent.substring(QUOTES, exponent.length() - QUOTES));

            BigInteger modulusValue = new BigInteger(POSITIVE_NUMBER, modulusBytes);
            BigInteger exponentValue = new BigInteger(POSITIVE_NUMBER, exponentBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulusValue, exponentValue);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException exception) {
            throw new IllegalStateException();
        }
    }
    // JWT에서 사용자 정보를 추출하고 이를 JSON객체로 변환한 다음 sub필드를 문자열로 추출하여 반환
    public String getAppleData(String socialAccessToken) {
        JsonArray publicKeyList = getApplePublicKeys();
        PublicKey publicKey = makePublicKey(socialAccessToken, publicKeyList);

        Claims userInfo = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(socialAccessToken.replaceFirst("Bearer ", ""))
                .getBody();

        JsonObject userInfoObject = (JsonObject) JsonParser.parseString(new Gson().toJson(userInfo));
        return userInfoObject.get("sub").getAsString();
    }
}