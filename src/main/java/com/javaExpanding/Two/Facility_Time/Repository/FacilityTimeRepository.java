package com.javaExpanding.Two.Facility_Time.Repository;

import com.javaExpanding.Two.Facility_Time.Database.FacilityTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityTimeRepository extends JpaRepository<FacilityTime, Integer> {
    List<FacilityTime> findByFacility_FacIdx(Integer facIdx);
}
