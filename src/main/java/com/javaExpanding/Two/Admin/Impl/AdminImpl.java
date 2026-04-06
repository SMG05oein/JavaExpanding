package com.javaExpanding.Two.Admin.Impl;

import com.javaExpanding.Two.Admin.Database.Admin;
import com.javaExpanding.Two.Admin.Dto.AdminLoginDto;
import com.javaExpanding.Two.Admin.Dto.AdminSignUpDto;
import com.javaExpanding.Two.Admin.Repository.AdminRepository;
import com.javaExpanding.Two.Admin.Service.AdminService;
import com.javaExpanding.Two.Error.MyError1;
import com.javaExpanding.Two.User.Database.RefreshToken;
import com.javaExpanding.Two.User.JwtUtil;
import com.javaExpanding.Two.User.Repository.RefreshTokenRepository;
import com.javaExpanding.Two.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    /*관리자 회원가입*/
    @Override
    @Transactional
    public void adminSignup(AdminSignUpDto dto) {
        if(adminRepository.existsByAdminId(dto.getAdminId())) {
            throw new RuntimeException("이미 존재하는 관리자 아이디입니다.");
        }
        if(adminRepository.existsByAdminEmail(dto.getAdminEmail())) {
            throw new RuntimeException("이미 존재하는 관리자 이메일입니다.");
        }
        if(userRepository.findByUserEmail(dto.getAdminEmail()).isPresent()){
            throw new RuntimeException("이미 유저로 회원가입이 되어있는 이메일입니다.");
        }

        Admin admin = Admin.builder()
                .adminId(dto.getAdminId())
                .adminPw(passwordEncoder.encode(dto.getAdminPw()))
                .adminEmail(dto.getAdminEmail())
                .adminName(dto.getAdminName())
                .adminStatus(Admin.AdminStatus.PENDING) // 처음엔 보류 상태
                .adminGrade(Admin.AdminGrade.NOTHING)   // 기본 권한
                .build();
        adminRepository.save(admin);
    }

    /*관리자 로그인*/
    @Override
    @Transactional
    public Map<String, String> adminLogin(AdminLoginDto dto) {
        Admin admin = adminRepository.findByAdminIdOrAdminEmail(dto.getUserIdorEmail(), dto.getUserIdorEmail())
                .orElseThrow(() -> new RuntimeException("관리자 정보를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getUserPw(), admin.getAdminPw())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if (admin.getAdminStatus() != Admin.AdminStatus.ACTIVE) {
            throw new RuntimeException("승인 대기 중이거나 활동이 중단된 관리자입니다.");
        }

        String accessToken = jwtUtil.createAccessToken(admin.getAdminId(), admin.getAdminGrade().name(),"ADMIN");
        String refreshToken = jwtUtil.createRefreshToken(admin.getAdminId());

        RefreshToken tokenEntity = refreshTokenRepository.findByUserEmail(admin.getAdminId())
                .orElse(new RefreshToken());

        tokenEntity.setUserEmail(admin.getAdminId());
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        refreshTokenRepository.save(tokenEntity);

        Map<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
//        result.put("adminName", admin.getAdminName());
//        result.put("adminGrade", admin.getAdminGrade().name());

        return result;
    }

    @Override
    @Transactional
    public void adminLogout(String adminId) {
        // DB에서 해당 관리자의 리프레시 토큰 삭제
        refreshTokenRepository.findByUserEmail(adminId)
                .ifPresent(refreshTokenRepository::delete);
    }

//    /*어드민이니?*/
//    public boolean isAdmin(String token) {
//        String adminId = jwtUtil.extractToken(token).getSubject();
//        return adminRepository.findByAdminId(adminId)
//                .map(admin -> admin.getAdminStatus() == Admin.AdminStatus.ACTIVE)
//                .orElse(false);
//    }
}
