package com.PuzzleU.Server.oauth;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();

    MultiValueMap<String, String>makeBody();
}
