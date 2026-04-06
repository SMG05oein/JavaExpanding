package com.javaExpanding.Two.User.Impl;

import com.javaExpanding.Two.Admin.Repository.AdminRepository;
import com.javaExpanding.Two.User.Database.RefreshToken;
import com.javaExpanding.Two.User.Database.Users;
import com.javaExpanding.Two.User.Dto.UserLoginDto;
import com.javaExpanding.Two.User.Dto.UserSignUpDto;
import com.javaExpanding.Two.User.JwtUtil;
import com.javaExpanding.Two.User.Repository.EmailAuthRepository;
import com.javaExpanding.Two.User.Repository.RefreshTokenRepository;
import com.javaExpanding.Two.User.Repository.UserRepository;
import com.javaExpanding.Two.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final EmailAuthRepository emailRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    /*회원가입*/
    @Override
    @Transactional
    public void signup(UserSignUpDto dto) {
        if(userRepository.findByUserEmail(dto.getUserEmail()).isPresent()){
            throw new RuntimeException("이미 회원가입이 되어있는 이메일입니다.");
        }
        if(adminRepository.existsByAdminEmail(dto.getUserEmail())) {
            throw new RuntimeException("이미 관리자로 회원가입이 되어있는 이메일입니다.");
        }
        Users user = new Users();
        user.setUserEmail(dto.getUserEmail());
        user.setUserPw(passwordEncoder.encode(dto.getUserPw()));
        user.setUserName(dto.getUserName());
        user.setUserStatus(Users.UserStatus.NORMAL);

        userRepository.save(user);

        emailRepo.deleteByEmail(dto.getUserEmail());
    }

    /*로그인*/
    @Override
    @Transactional
    public Map<String, String> login(UserLoginDto dto) {
        String email = dto.getUserEmail();
        String pw = dto.getUserPw();
        Users user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(pw, user.getUserPw())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.createAccessToken(email, user.getUserStatus().name(), "User");
        String refreshToken = jwtUtil.createRefreshToken(email);

        RefreshToken tokenEntity = refreshTokenRepository.findByUserEmail(email)
                .orElse(new RefreshToken());

        tokenEntity.setUserEmail(email);
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        refreshTokenRepository.save(tokenEntity);

        Map<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    /*로그아웃*/
    @Override
    @Transactional
    public void logout(String email) {
        refreshTokenRepository.findByUserEmail(email)
                .ifPresent(refreshTokenRepository::delete);
    }
}