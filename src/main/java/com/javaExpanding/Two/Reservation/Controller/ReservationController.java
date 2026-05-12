package com.javaExpanding.Two.Reservation.Controller;

import com.javaExpanding.Two.Reservation.Database.Reservation;
import com.javaExpanding.Two.Reservation.Dto.ReservationRequestDto;
import com.javaExpanding.Two.Reservation.Service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예약 관리", description = "예약 CRUD API")
@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약 생성", description = "로그인한 유저가 예약을 신청합니다.")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody ReservationRequestDto dto) {
        try {
            reservationService.createReservation(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("예약이 신청되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 신청 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "예약 수정", description = "본인의 예약을 수정합니다.")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/update/{resIdx}")
    public ResponseEntity<String> update(@PathVariable Integer resIdx, @Valid @RequestBody ReservationRequestDto dto) {
        try {
            reservationService.updateReservation(resIdx, dto);
            return ResponseEntity.ok("예약이 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 수정 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "예약 취소", description = "본인의 예약을 취소합니다.")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/cancel/{resIdx}")
    public ResponseEntity<String> cancel(@PathVariable Integer resIdx) {
        try {
            reservationService.cancelReservation(resIdx);
            return ResponseEntity.ok("예약이 취소되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 취소 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "내 예약 목록", description = "로그인한 유저의 예약 목록을 조회합니다.")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/my")
    public ResponseEntity<?> getMyReservations(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<Reservation> reservations = reservationService.getMyReservations(page);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("조회 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "전체 예약 목록", description = "관리자가 전체 예약 목록을 조회합니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @GetMapping("/list")
    public ResponseEntity<?> getAllReservations(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<Reservation> reservations = reservationService.getAllReservations(page);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("조회 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "예약 삭제", description = "관리자가 예약을 삭제합니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @DeleteMapping("/delete/{resIdx}")
    public ResponseEntity<String> delete(@PathVariable Integer resIdx) {
        try {
            reservationService.deleteReservation(resIdx);
            return ResponseEntity.ok("예약이 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
        }
    }
}
