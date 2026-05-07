package com.javaExpanding.Two.Facility.Database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "facilities")
@Getter @Setter
@NoArgsConstructor
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fac_idx")
    private Integer facIdx;

    @Column(name = "fac_name", nullable = false, length = 100)
    private String facName;

    @Column(name = "fac_location", nullable = false, length = 255)
    private String facLocation;

    @Column(name = "fac_description", columnDefinition = "TEXT")
    private String facDescription;

    @CreationTimestamp // 생성 시 자동 시간 입력
    @Column(name = "fac_create_dt", nullable = false, updatable = false)
    private LocalDateTime facCreateDt;

    @UpdateTimestamp // 수정 시 자동 시간 입력
    @Column(name = "fac_update_dt")
    private LocalDateTime facUpdateDt;
}