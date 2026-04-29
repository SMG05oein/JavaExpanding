package com.javaExpanding.Two.Reservation.Repository;

import com.javaExpanding.Two.Reservation.Entity.Reservation;
import com.javaExpanding.Two.Reservation.Entity.ReservationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    // ── 강의실 + 날짜 + 상태로 조회 (Enum 적용) ─────────────────────────
    // @EntityGraph: classroom을 JOIN FETCH해서 LazyInitializationException 방지
    @EntityGraph(attributePaths = "classroom")
    List<Reservation> findByClassroom_ClassroomIdAndDateAndStatus(
            String classroomId, LocalDate date, ReservationStatus status);

    // ── 사용자 본인 예약 조회 (fetch join으로 N+1 방지) ──────────────────
    @Query("""
        SELECT r FROM Reservation r
        JOIN FETCH r.classroom
        WHERE r.userEmail = :userEmail
        ORDER BY r.date ASC, r.startTime ASC
    """)
    List<Reservation> findByUserEmailWithClassroom(@Param("userEmail") String userEmail);

    // ── 중복 예약 검사 ────────────────────────────────────────────────────
    // COUNT(r) > 0 은 JPQL에서 SELECT절 직접 지원 안 됨 → COUNT만 반환 후 Java에서 비교
    @Query("""
        SELECT COUNT(r) FROM Reservation r
        WHERE r.classroom.classroomId = :classroomId
          AND r.date = :date
          AND r.status = com.javaExpanding.Two.Reservation.Entity.ReservationStatus.ACTIVE
          AND r.startTime < :endTime
          AND r.endTime   > :startTime
    """)
    long countConflict(
            @Param("classroomId") String classroomId,
            @Param("date")        LocalDate date,
            @Param("startTime")   LocalTime startTime,
            @Param("endTime")     LocalTime endTime);

    // ── 전체 예약 목록 (관리자용) — fetch join으로 N+1 방지 ──────────────
    @Query("""
        SELECT r FROM Reservation r
        JOIN FETCH r.classroom
        ORDER BY r.date ASC, r.startTime ASC
    """)
    List<Reservation> findAllWithClassroom();

    // ── 단건 조회 시에도 classroom fetch ────────────────────────────────
    @EntityGraph(attributePaths = "classroom")
    Optional<Reservation> findWithClassroomByReservationId(String reservationId);
}
