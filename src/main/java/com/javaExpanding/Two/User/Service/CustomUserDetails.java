package com.javaExpanding.Two.User.Service;

import com.javaExpanding.Two.Admin.Database.Admin;
import com.javaExpanding.Two.User.Database.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final String identifier; // 이메일 혹은 아이디
    private final String password;   // 인코딩된 비밀번호
    private final String role;       // 등급 (NORMAL, ADMIN1 등)
    private final String position;   // 구분 (User, Admin)
    private final Collection<? extends GrantedAuthority> authorities;

    // 1. 일반 유저용 생성자
    public CustomUserDetails(Users user) {
        this.identifier = user.getUserEmail();
        this.password = user.getUserPw();
        this.role = user.getUserStatus().name();
        this.position = "User";
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // 2. 관리자용 생성자
    public CustomUserDetails(Admin admin) {
        this.identifier = admin.getAdminId(); // 혹은 adminEmail
        this.password = admin.getAdminPw();
        this.role = admin.getAdminGrade().name();
        this.position = "Admin";
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return identifier;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}

