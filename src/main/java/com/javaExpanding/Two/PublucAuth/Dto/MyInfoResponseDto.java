package com.javaExpanding.Two.PublucAuth.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyInfoResponseDto {
    private String position;      // "User" 또는 "Admin"
    private Integer idx;          // 식별자 Index
    private String id;            // 아이디
    private String email;         // 이메일
    private String name;          // 이름
    private String status;        // 상태
    private String grade;         // 관리자 등급 (일반 회원의 경우 null)
    private LocalDateTime createDt; // 생성일
    private LocalDateTime updateDt; // 수정일
}
