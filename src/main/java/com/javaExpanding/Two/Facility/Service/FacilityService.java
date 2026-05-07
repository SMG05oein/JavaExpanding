package com.javaExpanding.Two.Facility.Service;

import com.javaExpanding.Two.Facility.Database.Facility;
import com.javaExpanding.Two.Facility.Dto.FacilityRequestDto;
import org.springframework.data.domain.Page;

public interface FacilityService {
    void createFacility(FacilityRequestDto dto); // 생성
    void updateFacility(Integer id, FacilityRequestDto dto); // 수정
    void deleteFacility(Integer id); // 삭제
    Page<Facility> getAllFacilities(int page); // 페이지네이션
}
