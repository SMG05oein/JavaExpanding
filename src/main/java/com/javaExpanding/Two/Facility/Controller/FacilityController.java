package com.javaExpanding.Two.Facility.Controller;

import com.javaExpanding.Two.Facility.Database.Facility;
import com.javaExpanding.Two.Facility.Dto.FacilityRequestDto;
import com.javaExpanding.Two.Facility.Service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "시설물 관리", description = "시설물 CRUD 및 페이지네이션 API")
@RestController
@RequestMapping("/api/facility")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    @Operation(summary = "시설물 등록", description = "어드민 권한이 있는 사용자만 시설물을 등록할 수 있습니다.")
//    @PreAuthorize("hasAuthority('ADMIN5') or hasAuthority('ADMIN')")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody FacilityRequestDto dto) {
        try {
//            System.out.println("접속자 권한: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            facilityService.createFacility(dto);
            return ResponseEntity.ok("시설물이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 중 오류 발생: " + e.getMessage());
        }
    }

    @Operation(summary = "시설물 수정", description = "어드민 권한이 있는 사용자만 시설물을 수정할 수 있습니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @Valid @RequestBody FacilityRequestDto dto) {
        try {
            facilityService.updateFacility(id, dto);
            return ResponseEntity.ok("시설 정보가 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 중 오류 발생");
        }
    }

    @Operation(summary = "시설물 목록 조회", description = "페이지네이션을 지원하며, 한 페이지에 10개씩 노출됩니다.")
    @GetMapping("/list")
    public ResponseEntity<?> getList(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<Facility> facilities = facilityService.getAllFacilities(page);
            return ResponseEntity.ok(facilities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("조회 중 예상치 못한 오류 발생");
        }
    }

    @Operation(summary = "시설물 삭제", description = "어드민 권한이 있는 사용자만 시설물을 삭제할 수 있습니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            facilityService.deleteFacility(id);
            return ResponseEntity.ok("시설물이 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("시설물을 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 예상치 못한 오류 발생");
        }
    }
}