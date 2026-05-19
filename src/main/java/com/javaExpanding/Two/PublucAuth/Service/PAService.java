package com.javaExpanding.Two.PublucAuth.Service;

import com.javaExpanding.Two.PublucAuth.Dto.MyInfoResponseDto;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface PAService {
    // 이메일 인증번호 보내기
    void sendVerificationEmail(String email);
    // 인증번호 확인
    boolean verifyCode(String email, String code);
    // 리프레시 토큰
    Map<String, String> refreshAccessToken(String refreshToken);
    // 아이디 중복 체크
    boolean existsByEmail(String email);
    // 로그인 했니?
    Map<String, Object> checkStatus();
    // 내 정보 조회
    MyInfoResponseDto getMyInfo();
}
