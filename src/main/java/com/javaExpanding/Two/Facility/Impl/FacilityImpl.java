package com.javaExpanding.Two.Facility.Impl;

import com.javaExpanding.Two.Facility.Database.Facility;
import com.javaExpanding.Two.Facility.Dto.FacilityRequestDto;
import com.javaExpanding.Two.Facility.Repository.FacilityRepository;
import com.javaExpanding.Two.Facility.Service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityImpl implements FacilityService {
    private final FacilityRepository facilityRepository;

    @Override
    @Transactional
    public void createFacility(FacilityRequestDto dto) {
        Facility facility = new Facility();
        facility.setFacName(dto.getFacName());
        facility.setFacLocation(dto.getFacLocation());
        facility.setFacDescription(dto.getFacDescription());

        facilityRepository.save(facility);
    }

    @Override
    @Transactional
    public void updateFacility(Integer id, FacilityRequestDto dto) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("시설물을 찾을 수 없습니다."));

        facility.setFacName(dto.getFacName());
        facility.setFacLocation(dto.getFacLocation());
        facility.setFacDescription(dto.getFacDescription());
        // @Transactional 덕분에 별도의 save 없이도 자동 업데이트(Dirty Checking)됩니다.
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Facility> getAllFacilities(int page) {
        // 한 페이지에 10개씩, 최신순(facIdx 내림차순) 정렬
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("facIdx").descending());
        return facilityRepository.findAll(pageRequest);
    }

    @Override
    @Transactional
    public void deleteFacility(Integer id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("시설물을 찾을 수 없습니다."));

        facilityRepository.deleteById(id);
    }
}