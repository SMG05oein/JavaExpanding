package com.javaExpanding.Two.Admin.Database;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminIdx;

    @Column(nullable = false, unique = true)
    private String adminId;

    @Column(nullable = false)
    private String adminPw;

    @Column(nullable = false)
    private String adminEmail;

    private String adminName;

    @Enumerated(EnumType.STRING)
    private AdminStatus adminStatus; // PENDING(보류), ACTIVE(활동), RESIGNED(퇴직)

    @Enumerated(EnumType.STRING)
    private AdminGrade adminGrade; // ADMIN1 ~ ADMIN5

    @CreationTimestamp
    private LocalDateTime adminCreateDt;

    @UpdateTimestamp
    private LocalDateTime adminUpdateDt;

    public enum AdminStatus { PENDING, ACTIVE, RESIGNED }
    public enum AdminGrade { NOTHING, ADMIN1, ADMIN2, ADMIN3, ADMIN4, ADMIN5 }
}