package com.javaExpanding.Two.Facility_Time.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilityTimeResponseDto {
    private Integer facTimeIdx;
    private String facDay;
    private String facOpen;
    private String facClose;
    private String facTimeStatus;
    private Integer facIdx;
}