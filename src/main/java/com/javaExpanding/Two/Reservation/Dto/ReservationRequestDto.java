package com.javaExpanding.Two.Reservation.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
public class ReservationRequestDto {

    @NotNull(message = "시설물 ID는 필수입니다.")
    private Integer facIdx;

    @NotNull(message = "예약일은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(type = "string", example = "2026-05-20")
    private LocalDate resDate;

    @NotNull(message = "시작 시각은 필수입니다.")
    @JsonFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", example = "14:00:00")
    private LocalTime resStart;

    @NotNull(message = "종료 시각은 필수입니다.")
    @JsonFormat(pattern = "HH:mm:ss")
    @Schema(type = "string", example = "16:00:00")
    private LocalTime resEnd;

    @NotBlank(message = "사용 목적은 필수입니다.")
    private String resPurpose;

    @NotNull(message = "인원수는 필수입니다.")
    @Min(value = 1, message = "인원수는 1명 이상이어야 합니다.")
    private Integer resHeadcount;
}
