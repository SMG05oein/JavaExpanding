package com.javaExpanding.Two.User.Controller;

import com.javaExpanding.Two.User.Dto.UserLoginDto;
import com.javaExpanding.Two.User.Dto.UserSignUpDto;
import com.javaExpanding.Two.User.JwtUtil;
import com.javaExpanding.Two.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name="회원 인증", description = "회원 인증 관련 API 명세서입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "이메일 인증 후 회원가입을 합니다.")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignUpDto dto) {
        try {
            userService.signup(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "로그인", description = "엑세스 토큰과 리프레시 토큰을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto dto) {
        try {
            return ResponseEntity.ok(userService.login(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일이나 비밀번호가 맞지 않습니다.");
        }
    }

    @Operation(summary = "로그아웃", description = "DB의 리프레시 토큰을 삭제하고 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String email) {
        try {
            userService.logout(email);
            return ResponseEntity.ok("로그아웃 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그아웃 처리 중 오류가 발생했습니다.");
        }
    }
}