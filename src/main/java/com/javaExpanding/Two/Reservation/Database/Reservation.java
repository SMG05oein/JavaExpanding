package com.javaExpanding.Two.Reservation.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
    name = "reservations",
    indexes = {
        @Index(name = "idx_reservation_lookup", columnList = "classroom_id, date, status")
    }
)
public class Reservation {

    @Id
    @Column(name = "reservation_id", length = 20)
    private String reservationId;

    /*
     * LAZY 유지 — Service 레이어에서 @EntityGraph로 필요한 곳만 즉시 로딩.
     * LazyInitializationException 방지.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail;

    @Column(nullable = false, length = 100)
    private String purpose;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // ── String → Enum 교체 (오타 버그 원천 차단) ──────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.ACTIVE;

    public Reservation() {}

    public Reservation(String reservationId, Classroom classroom, String userEmail,
                       String purpose, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.reservationId = reservationId;
        this.classroom     = classroom;
        this.userEmail     = userEmail;
        this.purpose       = purpose;
        this.date          = date;
        this.startTime     = startTime;
        this.endTime       = endTime;
        this.status        = ReservationStatus.ACTIVE;
    }

    public String getReservationId()       { return reservationId; }
    public Classroom getClassroom()        { return classroom; }
    public String getUserEmail()           { return userEmail; }
    public String getPurpose()             { return purpose; }
    public LocalDate getDate()             { return date; }
    public LocalTime getStartTime()        { return startTime; }
    public LocalTime getEndTime()          { return endTime; }
    public ReservationStatus getStatus()   { return status; }

    public void setReservationId(String id)      { this.reservationId = id; }
    public void setClassroom(Classroom c)         { this.classroom = c; }
    public void setUserEmail(String e)            { this.userEmail = e; }
    public void setPurpose(String p)              { this.purpose = p; }
    public void setDate(LocalDate d)              { this.date = d; }
    public void setStartTime(LocalTime t)         { this.startTime = t; }
    public void setEndTime(LocalTime t)           { this.endTime = t; }
    public void setStatus(ReservationStatus s)    { this.status = s; }
}
