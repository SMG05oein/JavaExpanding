package com.javaExpanding.Two.Facility_Time.Controller;

import com.javaExpanding.Two.Facility_Time.Database.FacilityTime;
import com.javaExpanding.Two.Facility_Time.Dto.FacilityTimeRequestDto;
import com.javaExpanding.Two.Facility_Time.Service.FacilityTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "시설물 운영 시간 관리", description = "시설물별 운영 시간 CRUD API")
@RestController
@RequestMapping("/api/facility-time")
@RequiredArgsConstructor
public class FacilityTimeController {
    private final FacilityTimeService facilityTimeService;

    @Operation(summary = "운영 시간 등록", description = "어드민 권한이 있는 사용자만 운영 시간을 등록할 수 있습니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody FacilityTimeRequestDto dto) {
        try {
            facilityTimeService.createFacilityTime(dto);
            return ResponseEntity.ok("운영 시간이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 중 오류 발생: " + e.getMessage());
        }
    }

    @Operation(summary = "운영 시간 수정", description = "어드민 권한이 있는 사용자만 운영 시간을 수정할 수 있습니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @Valid @RequestBody FacilityTimeRequestDto dto) {
        try {
            facilityTimeService.updateFacilityTime(id, dto);
            return ResponseEntity.ok("운영 시간 정보가 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 중 오류 발생");
        }
    }

    @Operation(summary = "시설물별 운영 시간 조회", description = "특정 시설물의 모든 운영 시간 정보를 조회합니다.")
    @GetMapping("/list/{facIdx}")
    public ResponseEntity<?> getList(@PathVariable Integer facIdx) {
        try {
            List<FacilityTime> times = facilityTimeService.getFacilityTimesByFacIdx(facIdx);
            return ResponseEntity.ok(times);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("조회 중 예상치 못한 오류 발생");
        }
    }

    @Operation(summary = "운영 시간 삭제", description = "어드민 권한이 있는 사용자만 운영 시간을 삭제할 수 있습니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            facilityTimeService.deleteFacilityTime(id);
            return ResponseEntity.ok("운영 시간 정보가 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 예상치 못한 오류 발생");
        }
    }
}
