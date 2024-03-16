package com.PuzzleU.Server.apple;

import com.PuzzleU.Server.common.enumSet.LoginType;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final int ACCESS_TOKEN_EXPIRATION = 7200000;
    private static final int REFRESH_TOKEN_EXPIRATION = 1209600000;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private final AppleService appleService;
    @Transactional
    public SignInResponse signIn(String socialAccessToken, SignInRequest request) {
        User member = getMember(socialAccessToken, request);
        Token token = getToken(member);
        return SignInResponse.of(token,true);
    }

    @Transactional
    public void signOut(long memberId) {
        User member = findMember(memberId);
        member.resetRefreshToken();
    }

    @Transactional
    public void withdraw(long memberId) {
        User member = findMember(memberId);
        deleteMember(member);
    }

    private User getMember(String socialAccessToken, SignInRequest request) {
        LoginType socialType = request.socialType();
        String socialId = getSocialId(socialAccessToken, socialType);
        return signUp(socialType, socialId);
    }

    private String getSocialId(String socialAccessToken, LoginType socialType) {
        return appleService.getAppleData(socialAccessToken);
    }

    private User signUp(LoginType socialType, String socialId) {
        return userRepository.findByLoginTypeAndUsername(socialType, socialId)
                .orElseGet(() -> saveMember(socialType, socialId));
    }

    private User saveMember(LoginType socialType, String socialId) {
        User member = User.builder()
                .socialType(socialType)
                .socialId(socialId)
                .build();
        return userRepository.save(member);
    }

    private Token getToken(User member) {
        Token token = generateToken(new UserAuthentication(member.getId(), null, null));
        member.updateRefreshToken(token.getRefreshToken());
        return token;
    }

    private Token generateToken(Authentication authentication) {
        return Token.builder()
                .accessToken(jwtTokenProvider.generateToken(authentication, ACCESS_TOKEN_EXPIRATION))
                .refreshToken(jwtTokenProvider.generateToken(authentication, REFRESH_TOKEN_EXPIRATION))
                .build();
    }

    private User findMember(long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }

    private void deleteMember(User member) {
        userRepository.delete(member);
    }
}