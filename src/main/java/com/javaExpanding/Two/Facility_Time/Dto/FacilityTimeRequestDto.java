package com.javaExpanding.Two.Facility_Time.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javaExpanding.Two.Facility_Time.Database.FacilityTime.FacDay;
import com.javaExpanding.Two.Facility_Time.Database.FacilityTime.FacTimeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class FacilityTimeRequestDto {
    @NotNull(message = "시설물 ID는 필수입니다.")
    private Integer facIdx;

    @NotNull(message = "요일은 필수입니다.")
    private FacDay facDay;

    @NotNull(message = "오픈 시간은 필수입니다.")
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", pattern = "HH:mm", example = "09:00")
    private LocalTime facOpen;

    @NotNull(message = "마감 시간은 필수입니다.")
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", pattern = "HH:mm", example = "18:00")
    private LocalTime facClose;

    @NotNull(message = "상태는 필수입니다.")
    private FacTimeStatus facTimeStatus;
}
