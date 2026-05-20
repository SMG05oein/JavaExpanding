package com.javaExpanding.Two.Facility_Time.Service;

import com.javaExpanding.Two.Facility_Time.Database.FacilityTime;
import com.javaExpanding.Two.Facility_Time.Dto.FacilityTimeRequestDto;
import com.javaExpanding.Two.Facility_Time.Dto.FacilityTimeResponseDto;

import java.util.List;

public interface FacilityTimeService {
    void createFacilityTime(FacilityTimeRequestDto dto);
    void updateFacilityTime(Integer id, FacilityTimeRequestDto dto);
    void deleteFacilityTime(Integer id);
    List<FacilityTimeResponseDto> getFacilityTimesByFacIdx(Integer facIdx);}
