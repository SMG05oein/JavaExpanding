package com.javaExpanding.Two.Admin.Controller;

import com.javaExpanding.Two.Admin.Dto.AdminLoginDto;
import com.javaExpanding.Two.Admin.Dto.AdminSignUpDto;
import com.javaExpanding.Two.Admin.Service.AdminService;
import com.javaExpanding.Two.Error.MyError1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="관리자 인증", description = "관리자 인증 관련 API 명세서입니다.")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "관리자 회원가입", description = "회원가입을 합니다.")
    @PostMapping("/signup")
    public ResponseEntity<String> sendEmail(@RequestBody AdminSignUpDto dto) {
        try {
            adminService.adminSignup(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "관리자 로그인", description = "아이디 또는 이메일로 로그인을 시도합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginDto dto) {
        try {
            return ResponseEntity.ok(adminService.adminLogin(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "관리자 로그아웃", description = "로그아웃 처리를 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String adminId) {
        try {
            adminService.adminLogout(adminId);
            return ResponseEntity.ok("로그아웃 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
