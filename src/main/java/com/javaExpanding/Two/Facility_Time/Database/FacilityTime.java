package com.javaExpanding.Two.Facility_Time.Database;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaExpanding.Two.Facility.Database.Facility;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "facility_time")
@Getter @Setter
@NoArgsConstructor
public class FacilityTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fac_time_idx")
    private Integer facTimeIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_fac_idx", nullable = false)
    private Facility facility;

    @Enumerated(EnumType.STRING)
    @Column(name = "fac_day", nullable = false)
    private FacDay facDay;

    @Column(name = "fac_open", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", pattern = "HH:mm", example = "09:00")
    private LocalTime facOpen;

    @Column(name = "fac_close", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", pattern = "HH:mm", example = "18:00")
    private LocalTime facClose;

    @Enumerated(EnumType.STRING)
    @Column(name = "fac_time_status", nullable = false)
    private FacTimeStatus facTimeStatus;

    @CreationTimestamp
    @Column(name = "fac_time_create_dt", nullable = false, updatable = false)
    private LocalDateTime facTimeCreateDt;

    @UpdateTimestamp
    @Column(name = "fac_time_update_dt")
    private LocalDateTime facTimeUpdateDt;

    public enum FacDay { 월, 화, 수, 목, 금, 토, 일 }
    public enum FacTimeStatus { 운영중, 예약불가, 점검중 }
}
