package com.javaExpanding.Two.Facility.Repository;

import com.javaExpanding.Two.Facility.Database.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {
}
