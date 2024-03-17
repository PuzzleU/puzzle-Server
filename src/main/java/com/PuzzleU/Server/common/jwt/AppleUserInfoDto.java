package com.PuzzleU.Server.common.jwt;

import com.PuzzleU.Server.common.enumSet.LoginType;
import com.PuzzleU.Server.common.enumSet.UserRoleEnum;
import com.PuzzleU.Server.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class AppleUserInfoDto {
    private String username;
    private String email;
    private String id;
    private String refreshToken;
    private String deviceToken;
    private LoginType loginType;

    public AppleUserInfoDto(String username, String email, String id, String refreshToken, String deviceToken, LoginType loginType) {
        this.username = username;
        this.email = email;
        this.id = id;
        this.refreshToken = refreshToken;
        this.deviceToken = deviceToken;
        this.loginType = loginType;
    }
    // UserRequestDto를 User entity로 변환하여 return
    public User toEntity()
    {

        return new User(this.username, this.email, this.id, UserRoleEnum.USER, this.refreshToken, this.loginType);
    }
}
