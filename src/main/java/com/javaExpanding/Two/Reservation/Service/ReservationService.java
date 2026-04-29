package com.javaExpanding.Two.Reservation.Service;

import com.javaExpanding.Two.Reservation.Dto.ReservationDto;
import com.javaExpanding.Two.Reservation.Entity.Classroom;
import com.javaExpanding.Two.Reservation.Entity.Reservation;
import com.javaExpanding.Two.Reservation.Entity.ReservationStatus;
import com.javaExpanding.Two.Reservation.Repository.ClassroomRepository;
import com.javaExpanding.Two.Reservation.Repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final ClassroomRepository   classroomRepo;

    public ReservationService(ReservationRepository reservationRepo,
                              ClassroomRepository classroomRepo) {
        this.reservationRepo = reservationRepo;
        this.classroomRepo   = classroomRepo;
    }

    // ── 강의실 전체 조회 ────────────────────────────────────────────────
    public List<Classroom> getAllClassrooms() {
        return classroomRepo.findAll();
    }

    // ── 예약 생성 (User 전용) ───────────────────────────────────────────
    @Transactional
    public ReservationDto.Response createReservation(String userEmail,
                                                     ReservationDto.CreateRequest req) {
        // 1. 강의실 존재 확인
        Classroom classroom = classroomRepo.findById(req.getClassroomId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의실입니다."));

        // 2. 시간 유효성 검사
        if (!req.getEndTime().isAfter(req.getStartTime())) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 늦어야 합니다.");
        }

        // 3. 중복 예약 검사 — countConflict > 0 으로 비교 (JPQL COUNT > 0 직접 반환 불가 수정)
        boolean conflict = reservationRepo.countConflict(
            req.getClassroomId(), req.getDate(),
            req.getStartTime(), req.getEndTime()) > 0;

        if (conflict) {
            throw new IllegalStateException("해당 시간대에 이미 예약이 있습니다.");
        }

        // 4. ID 생성 — 충돌 시 재시도 (UUID substring 충돌 방어)
        String id = generateUniqueId();

        Reservation reservation = new Reservation(
            id, classroom, userEmail,
            req.getPurpose(), req.getDate(),
            req.getStartTime(), req.getEndTime());

        return ReservationDto.Response.from(reservationRepo.save(reservation));
    }

    // ── 내 예약 목록 (User) — fetch join으로 N+1 방지 ──────────────────
    public List<ReservationDto.Response> getMyReservations(String userEmail) {
        return reservationRepo.findByUserEmailWithClassroom(userEmail)
            .stream()
            .map(ReservationDto.Response::from)
            .collect(Collectors.toList());
    }

    // ── 예약 취소 (User: 본인 예약만) ───────────────────────────────────
    @Transactional
    public void cancelReservation(String reservationId, String userEmail) {
        // fetch join으로 classroom 함께 로딩 (Response.from 내부 접근 대비)
        Reservation r = reservationRepo.findWithClassroomByReservationId(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (!r.getUserEmail().equals(userEmail)) {
            throw new SecurityException("본인 예약만 취소할 수 있습니다.");
        }
        if (r.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }
        r.setStatus(ReservationStatus.CANCELLED);   // Enum 사용
    }

    // ── 전체 예약 목록 (Admin 전용) — fetch join으로 N+1 방지 ───────────
    public List<ReservationDto.Response> getAllReservations() {
        return reservationRepo.findAllWithClassroom()
            .stream()
            .map(ReservationDto.Response::from)
            .collect(Collectors.toList());
    }

    // ── 관리자 예약 강제 취소 ───────────────────────────────────────────
    @Transactional
    public void adminCancelReservation(String reservationId) {
        Reservation r = reservationRepo.findWithClassroomByReservationId(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (r.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }
        r.setStatus(ReservationStatus.CANCELLED);   // Enum 사용
    }

    // ── 강의실별 예약 현황 (공개) ───────────────────────────────────────
    public List<ReservationDto.Response> getClassroomReservations(String classroomId,
                                                                   LocalDate date) {
        return reservationRepo
            .findByClassroom_ClassroomIdAndDateAndStatus(
                classroomId, date, ReservationStatus.ACTIVE)   // Enum 사용
            .stream()
            .map(ReservationDto.Response::from)
            .collect(Collectors.toList());
    }

    // ── UUID 충돌 방어: 최대 5회 재시도 ────────────────────────────────
    private String generateUniqueId() {
        for (int i = 0; i < 5; i++) {
            String candidate = UUID.randomUUID().toString()
                .replace("-", "").substring(0, 12).toUpperCase();
            if (!reservationRepo.existsById(candidate)) {
                return candidate;
            }
        }
        // 극히 드문 경우 — 전체 UUID 사용 (32자)
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
