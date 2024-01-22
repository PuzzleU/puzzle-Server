package com.PuzzleU.Server.oauth;

import com.PuzzleU.Server.entity.enumSet.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
