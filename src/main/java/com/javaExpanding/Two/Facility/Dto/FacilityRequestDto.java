package com.javaExpanding.Two.Facility.Dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityRequestDto {
    @NotBlank(message = "시설물 이름은 필수입니다.")
    private String facName;
    @NotBlank(message = "시설물 위치는 필수입니다.")
    private String facLocation;
    private String facDescription;
}
