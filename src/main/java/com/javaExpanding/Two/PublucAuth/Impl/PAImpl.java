package com.javaExpanding.Two.PublucAuth.Impl;

import com.javaExpanding.Two.Admin.Database.Admin;
import com.javaExpanding.Two.Admin.Repository.AdminRepository;
import com.javaExpanding.Two.PublucAuth.Service.PAService;
import com.javaExpanding.Two.User.Database.EmailAuth;
import com.javaExpanding.Two.User.Database.RefreshToken;
import com.javaExpanding.Two.User.Database.Users;
import com.javaExpanding.Two.User.JwtUtil;
import com.javaExpanding.Two.User.Repository.EmailAuthRepository;
import com.javaExpanding.Two.User.Repository.RefreshTokenRepository;
import com.javaExpanding.Two.User.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PAImpl implements PAService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final EmailAuthRepository emailRepo;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    /*메일 보내기*/
    @Override
    @Transactional
    public void sendVerificationEmail(String email) {
        // 1. 백석대학교 이메일 형식 체크 (@bu.ac.kr)
        if (!email.endsWith("@bu.ac.kr")) {
            throw new IllegalArgumentException("학교 계정(@bu.ac.kr)만 가입 가능합니다.");
        }

        // 2. 이미 가입된 이메일인지 체크
        if (existsByEmail(email)) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        // 3. 6자리 인증번호 생성 (100000~999999)
        String code = String.valueOf((int)(Math.random() * 899999) + 100000);

        // 4. 기존 인증 정보가 있다면 삭제 후 새로 저장
        emailRepo.deleteByEmail(email);

        EmailAuth auth = new EmailAuth();
        auth.setEmail(email);
        auth.setVerificationCode(code);
        auth.setExpiryDate(LocalDateTime.now().plusMinutes(5)); // 5분 제한
        emailRepo.save(auth);

        // 5. 이메일 발송
        sendRealEmail(email, code);
    }

    private void sendRealEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);;
        message.setSubject("[백석대학교 통합 예약 시스템] 회원가입 인증 번호입니다.");
        message.setText("인증 번호: " + code + "\n5분 이내에 입력해주세요.");
        mailSender.send(message);
    }

    /*이멜 중복 체크*/
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByUserEmail(email).isPresent() ||
                adminRepository.findByAdminIdOrAdminEmail(email, email).isPresent();    }

    /*인증번호 인증하기*/
    @Override
    @Transactional(readOnly = true)
    public boolean verifyCode(String email, String code) {
        return emailRepo.findByEmail(email)
                .map(v -> v.getVerificationCode().equals(code) && // 번호 일치 여부
                        v.getExpiryDate().isAfter(LocalDateTime.now())) // 시간 만료 여부
                .orElse(false);
    }

    /* 리프레시 토큰으로 액세스 토큰 재발급*/
    @Override
    @Transactional
    public Map<String, String> refreshAccessToken(String refreshToken) {
        // 1. 리프레시 토큰에서 식별자(Email) 추출
        Claims claims = jwtUtil.extractToken(refreshToken);
        String identifier = claims.getSubject();

        // 2. DB에서 해당 식별자의 리프레시 토큰 실존 여부 확인
        RefreshToken savedToken = refreshTokenRepository.findByUserEmail(identifier)
                .orElseThrow(() -> new RuntimeException("로그인 정보가 없습니다."));

        if (!savedToken.getToken().equals(refreshToken) || savedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효하지 않거나 만료된 리프레시 토큰입니다.");
        }

        // 3. [핵심] DB를 조회하여 현재 사용자의 정확한 Role과 Position 파악
        String role;
        String position;

        // 먼저 유저 테이블에서 찾아보고, 없으면 관리자 테이블에서 찾습니다.
        Optional<Users> userOpt = userRepository.findByUserEmail(identifier);
        if (userOpt.isPresent()) {
            role = userOpt.get().getUserStatus().name(); // NORMAL 등
            position = "User";
        } else {
            Admin admin = adminRepository.findByAdminIdOrAdminEmail(identifier, identifier)
                    .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
            role = admin.getAdminGrade().name(); // ADMIN5 등
            position = "Admin";
        }

        // 4. 리프레시 토큰 갱신 (10분 미만 시)
        String newRefreshToken = refreshToken;
        if (savedToken.getExpiryDate().isBefore(LocalDateTime.now().plusMinutes(10))) {
            newRefreshToken = jwtUtil.createRefreshToken(identifier);
            savedToken.setToken(newRefreshToken);
            savedToken.setExpiryDate(LocalDateTime.now().plusMinutes(40));
            refreshTokenRepository.save(savedToken);
        }

        // 5. 이제 null이 아닌 정확한 정보로 액세스 토큰 재발급
        String newAccessToken = jwtUtil.createAccessToken(identifier, role, position);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);
        return tokens;
    }
    @Override
    public Map<String, Object> checkStatus(String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 1. 토큰 검증 및 파싱
            Claims claims = jwtUtil.extractToken(token);

            // 2. 토큰 내부 정보 추출
            String identifier = claims.getSubject(); // 이메일 혹은 아이디
            String role = jwtUtil.getRole(token); // USER 또는 ADMIN
            String position = jwtUtil.getPosition(token); // USER 또는 ADMIN

            response.put("isLoggedIn", true);
            response.put("id", identifier);
            response.put("grade", position);
            response.put("role", role);
            // 필요하다면 등급(grade) 정보도 여기서 추출 가능

        } catch (Exception e) {
            response.put("isLoggedIn", false);
            response.put("message", "유효하지 않거나 만료된 세션입니다.");
        }
        return response;
    }
}
