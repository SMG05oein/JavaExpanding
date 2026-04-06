package com.javaExpanding.Two.User.Database;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Integer userIdx; // PK

    @Column(name = "user_email", nullable = false, unique = true, length = 255)
    private String userEmail; // 로그인 이메일

    @Column(name = "user_pw", nullable = false, length = 255)
    private String userPw; // 비밀번호

    @Column(name = "user_name", length = 50)
    private String userName; // 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus = UserStatus.NORMAL; // 유저 상태

    @CreationTimestamp
    @Column(name = "user_create_dt", nullable = false, updatable = false)
    private LocalDateTime userCreateDt; // 생성일

    @UpdateTimestamp
    @Column(name = "user_update_dt")
    private LocalDateTime userUpdateDt; // 수정일

    // Enum 정의 (일반, 정지 등)
    public enum UserStatus {
        NORMAL, PENALTY1, PENALTY2, PENALTY3, PENALTY4, PENALTY5
    }
}