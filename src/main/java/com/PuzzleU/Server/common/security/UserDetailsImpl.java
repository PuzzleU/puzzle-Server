package com.PuzzleU.Server.common.security;

import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.common.enumSet.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//Security를 사용하기 위해서는 UserDetails를 만들어줘야한다
// UserDetails는 사용자의 정보를 제공하는 인터페이스
public class UserDetailsImpl implements UserDetails {


    private final User user;
    private final String username;

    // 인증이 완료된 사용자 추가하기
    public UserDetailsImpl(User user, String username) {
        this.user = user;
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 유저의 role을 가져오고 그 role에서 authority를 가져온다
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();
        // simplegrantedauthority 클래스를 사용하여 권한을 spring security에서 인식되는 형태로 변환 한다
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority>authorities = new ArrayList<>();
        // 권한들을 모두찾고 그 권환된 것들을 authorites에 추가해준다
        authorities.add(simpleGrantedAuthority);

        return authorities; // GrantedAuthority로 추상화된 사용자 권한 반환
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
