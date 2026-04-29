package com.javaExpanding.Two.Reservation.Controller;

import com.javaExpanding.Two.Reservation.Dto.ReservationDto;
import com.javaExpanding.Two.Reservation.Entity.Classroom;
import com.javaExpanding.Two.Reservation.Service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Reservation (User)", description = "학생 강의실 예약 API")
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    // ── 강의실 목록 (로그인 없이 조회 가능) ────────────────────────────
    @Operation(summary = "강의실 목록 조회")
    @GetMapping("/classrooms")
    public ResponseEntity<List<Classroom>> getClassrooms() {
        return ResponseEntity.ok(service.getAllClassrooms());
    }

    // ── 강의실별 예약 현황 ───────────────────────────────────────────────
    // ❌ 수정 전: @RequestParam(defaultValue = "#{SpEL}") — SpEL 미지원으로 파싱 예외
    // ✅ 수정 후: required=false + Java에서 null 처리
    @Operation(summary = "강의실 예약 현황 조회")
    @GetMapping("/classrooms/{classroomId}/schedule")
    public ResponseEntity<List<ReservationDto.Response>> getSchedule(
            @PathVariable String classroomId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // date 미입력 시 오늘 날짜로 기본값 처리 (Java에서 안전하게)
        LocalDate target = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(service.getClassroomReservations(classroomId, target));
    }

    // ── 예약 생성 (User만 가능) ─────────────────────────────────────────
    // ✅ @Valid 추가: CreateRequest의 @NotBlank/@Future 검증 활성화
    @Operation(summary = "강의실 예약", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('User')")
    @PostMapping
    public ResponseEntity<ReservationDto.Response> create(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody ReservationDto.CreateRequest req) {

        return ResponseEntity.ok(service.createReservation(user.getUsername(), req));
    }

    // ── 내 예약 목록 ────────────────────────────────────────────────────
    @Operation(summary = "내 예약 목록", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('User')")
    @GetMapping("/my")
    public ResponseEntity<List<ReservationDto.Response>> myReservations(
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(service.getMyReservations(user.getUsername()));
    }

    // ── 예약 취소 (본인만) ──────────────────────────────────────────────
    @Operation(summary = "예약 취소", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('User')")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Map<String, String>> cancel(
            @PathVariable String reservationId,
            @AuthenticationPrincipal UserDetails user) {

        service.cancelReservation(reservationId, user.getUsername());
        return ResponseEntity.ok(Map.of("message", "예약이 취소되었습니다."));
    }
}
