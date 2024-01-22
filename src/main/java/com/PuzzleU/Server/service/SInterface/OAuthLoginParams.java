package com.PuzzleU.Server.service.SInterface;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;
import org.springframework.util.MultiValueMap;


/*
 * // 카카오 요청에 필요한 데이터를 가지고 있는 파라미터
 */

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();

    MultiValueMap<String, String>makeBody();
}
