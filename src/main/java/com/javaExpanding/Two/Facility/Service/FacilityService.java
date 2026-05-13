package com.javaExpanding.Two.Facility.Service;

import com.javaExpanding.Two.Facility.Database.Facility;
import com.javaExpanding.Two.Facility.Dto.FacilityRequestDto;
import org.springframework.data.domain.Page;

import com.javaExpanding.Two.Facility.Dto.FacilityNameProjection;

import java.util.List;

public interface FacilityService {
    void createFacility(FacilityRequestDto dto); // 생성
    void updateFacility(Integer id, FacilityRequestDto dto); // 수정
    void deleteFacility(Integer id); // 삭제
    Page<Facility> getAllFacilities(int page); // 페이지네이션
    List<FacilityNameProjection> getAllFacilities(); // 전체 목록 조회 (이름만)
}
