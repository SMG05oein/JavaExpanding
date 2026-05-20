package com.javaExpanding.Two.Reservation.Database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javaExpanding.Two.Facility.Database.Facility;
import com.javaExpanding.Two.User.Database.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservation")
@Getter @Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_idx")
    private Integer resIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_idx", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_fac_idx", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Facility facility;

    @Column(name = "res_date", nullable = false)
    private LocalDate resDate;

    @Column(name = "res_start", nullable = false)
    private LocalTime resStart;

    @Column(name = "res_end", nullable = false)
    private LocalTime resEnd;

    @Column(name = "res_purpose", nullable = false, columnDefinition = "TEXT")
    private String resPurpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "res_status", nullable = false)
    private ResStatus resStatus = ResStatus.대기;

    @Column(name = "res_headcount", nullable = false)
    private Integer resHeadcount;

    @CreationTimestamp
    @Column(name = "res_create_dt", nullable = false, updatable = false)
    private LocalDateTime resCreateDt;

    @UpdateTimestamp
    @Column(name = "res_update_dt")
    private LocalDateTime resUpdateDt;

    @Transient
    private String rejectReason;

    public enum ResStatus { 대기, 승인, 거절, 취소 }
}
