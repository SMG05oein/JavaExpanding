package com.javaExpanding.Two.Facility_Time.Impl;

import com.javaExpanding.Two.Facility.Database.Facility;
import com.javaExpanding.Two.Facility.Repository.FacilityRepository;
import com.javaExpanding.Two.Facility_Time.Database.FacilityTime;
import com.javaExpanding.Two.Facility_Time.Dto.FacilityTimeRequestDto;
import com.javaExpanding.Two.Facility_Time.Dto.FacilityTimeResponseDto;
import com.javaExpanding.Two.Facility_Time.Repository.FacilityTimeRepository;
import com.javaExpanding.Two.Facility_Time.Service.FacilityTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityTimeImpl implements FacilityTimeService {
    private final FacilityTimeRepository facilityTimeRepository;
    private final FacilityRepository facilityRepository;

    @Override
    @Transactional
    public void createFacilityTime(FacilityTimeRequestDto dto) {
        Facility facility = facilityRepository.findById(dto.getFacIdx())
                .orElseThrow(() -> new RuntimeException("시설물을 찾을 수 없습니다."));

        FacilityTime facilityTime = new FacilityTime();
        facilityTime.setFacility(facility);
        facilityTime.setFacDay(dto.getFacDay());
        facilityTime.setFacOpen(dto.getFacOpen());
        facilityTime.setFacClose(dto.getFacClose());
        facilityTime.setFacTimeStatus(dto.getFacTimeStatus());

        facilityTimeRepository.save(facilityTime);
    }

    @Override
    @Transactional
    public void updateFacilityTime(Integer id, FacilityTimeRequestDto dto) {
        FacilityTime facilityTime = facilityTimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("시설물 시간 정보를 찾을 수 없습니다."));

        facilityTime.setFacDay(dto.getFacDay());
        facilityTime.setFacOpen(dto.getFacOpen());
        facilityTime.setFacClose(dto.getFacClose());
        facilityTime.setFacTimeStatus(dto.getFacTimeStatus());
    }

    @Override
    @Transactional
    public void deleteFacilityTime(Integer id) {
        if (!facilityTimeRepository.existsById(id)) {
            throw new RuntimeException("시설물 시간 정보를 찾을 수 없습니다.");
        }
        facilityTimeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityTimeResponseDto> getFacilityTimesByFacIdx(Integer facIdx) {
        List<FacilityTime> times = facilityTimeRepository.findByFacility_FacIdx(facIdx);

        // Entity 리스트를 DTO 리스트로 변환 (Jackson 에러 해결)
        return times.stream().map(time -> {
            FacilityTimeResponseDto responseDto = new FacilityTimeResponseDto();
            responseDto.setFacTimeIdx(time.getFacTimeIdx());
            responseDto.setFacDay(time.getFacDay().name());
            responseDto.setFacOpen(time.getFacOpen().toString());
            responseDto.setFacClose(time.getFacClose().toString());
            responseDto.setFacTimeStatus(time.getFacTimeStatus().name());
            responseDto.setFacIdx(time.getFacility().getFacIdx());
            return responseDto;
        }).collect(Collectors.toList());
    }
}
