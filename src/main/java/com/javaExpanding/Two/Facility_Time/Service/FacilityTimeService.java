package com.javaExpanding.Two.Facility_Time.Service;

import com.javaExpanding.Two.Facility_Time.Database.FacilityTime;
import com.javaExpanding.Two.Facility_Time.Dto.FacilityTimeRequestDto;

import java.util.List;

public interface FacilityTimeService {
    void createFacilityTime(FacilityTimeRequestDto dto);
    void updateFacilityTime(Integer id, FacilityTimeRequestDto dto);
    void deleteFacilityTime(Integer id);
    List<FacilityTime> getFacilityTimesByFacIdx(Integer facIdx);
}
