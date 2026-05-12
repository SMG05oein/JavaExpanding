package com.javaExpanding.Two.Approval.Database;

import com.javaExpanding.Two.Admin.Database.Admin;
import com.javaExpanding.Two.Reservation.Database.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval")
@Getter @Setter
@NoArgsConstructor
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_idx")
    private Integer appIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_res_idx", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_admin_idx", nullable = false)
    private Admin admin;

    @Column(name = "app_isApprov", nullable = false)
    private Boolean appIsApprov; // true: 승인, false: 반려

    @Column(name = "app_comment", columnDefinition = "TEXT")
    private String appComment;

    @CreationTimestamp
    @Column(name = "app_create_dt", nullable = false, updatable = false)
    private LocalDateTime appCreateDt;

    @UpdateTimestamp
    @Column(name = "app_update_dt")
    private LocalDateTime appUpdateDt;
}
