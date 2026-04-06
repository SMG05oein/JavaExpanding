package com.javaExpanding.Two.User.Service;

import com.javaExpanding.Two.User.Dto.UserLoginDto;
import com.javaExpanding.Two.User.Dto.UserSignUpDto;

import java.util.Map;

public interface UserService {
    // 회원가입
    void signup(UserSignUpDto usersFullDto);
    //로그인
    Map<String, String> login(UserLoginDto userLoginDto);
    // 로그아웃
    void logout(String email);
}
