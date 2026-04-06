package com.javaExpanding.Two.User.Database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String verificationCode; // 6자리 인증번호

    @Column(nullable = false)
    private LocalDateTime expiryDate; // 만료 시간 (예: 생성 후 5분)

    @CreationTimestamp
    private LocalDateTime createdDt;
}
