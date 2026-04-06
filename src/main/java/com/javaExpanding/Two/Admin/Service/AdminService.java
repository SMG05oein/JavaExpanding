package com.javaExpanding.Two.Admin.Service;

import com.javaExpanding.Two.Admin.Dto.AdminLoginDto;
import com.javaExpanding.Two.Admin.Dto.AdminSignUpDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public interface AdminService {
    void adminSignup(AdminSignUpDto dto);

    /* 관리자 로그인 (아이디 또는 이메일) */
    Map<String, String> adminLogin(AdminLoginDto dto);

    /* 관리자 로그아웃 */
    void adminLogout(String adminId);
}
