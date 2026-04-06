package com.javaExpanding.Two.PublucAuth.Controller;

import com.javaExpanding.Two.PublucAuth.Service.PAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name="공통 인증", description = "이메일 전송, JWT 관련 API 명세서입니다.")
@RestController
@RequestMapping("/api/public_auh")
@RequiredArgsConstructor
public class PAController {
    private final PAService paService;

    @Operation(summary = "인증 이메일 발송", description = "@bu.ac.kr 계정인지 확인 후 인증번호를 보냅니다.")
    @PostMapping("/send_email")
    public ResponseEntity<String> sendEmail(@RequestParam String email) {
        try {
            paService.sendVerificationEmail(email);
            return ResponseEntity.ok("인증 번호가 발송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 보내면 새 액세스 토큰, 리프레시 토큰을 줍니다.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
        try {
            Map<String, String> tokens = paService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 만료되었습니다. 다시 로그인해주세요.");
        }
    }

    @Operation(summary = "인증번호 확인", description = "입력한 인증번호가 맞는지 확인합니다.")
    @PostMapping("/verify_code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = paService.verifyCode(email, code);

        if (isVerified) {
            return ResponseEntity.ok("인증에 성공하였습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 틀렸거나 만료되었습니다.");
        }
    }

    @Operation(summary = "로그인 상태 체크", description = "토큰을 기반으로 사용자 정보를 반환합니다.")
    @PostMapping("/check_status")
    public ResponseEntity<?> checkStatus() {
        try {
            Map<String, Object> response = new HashMap<>();
            response = paService.checkStatus();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않거나 만료된 세션입니다.");

        }
    }

}
