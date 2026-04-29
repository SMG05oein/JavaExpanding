package com.javaExpanding.Two.Reservation.Dto;

import com.javaExpanding.Two.Reservation.Entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDto {

    // ───────────────────────────────────────────────
    //  Request — Bean Validation 추가
    // ───────────────────────────────────────────────
    @Schema(description = "예약 생성 요청")
    public static class CreateRequest {

        @NotBlank(message = "강의실 ID는 필수입니다.")
        @Schema(description = "강의실 ID", example = "C101")
        private String classroomId;

        @NotBlank(message = "사용 목적은 필수입니다.")
        @Schema(description = "사용 목적", example = "팀 프로젝트 회의")
        private String purpose;

        @NotNull(message = "날짜는 필수입니다.")
        @Future(message = "예약 날짜는 오늘 이후여야 합니다.")
        @Schema(description = "예약 날짜 (yyyy-MM-dd)", example = "2025-06-01")
        private LocalDate date;

        @NotNull(message = "시작 시간은 필수입니다.")
        @Schema(description = "시작 시간 (HH:mm)", example = "10:00")
        private LocalTime startTime;

        @NotNull(message = "종료 시간은 필수입니다.")
        @Schema(description = "종료 시간 (HH:mm)", example = "12:00")
        private LocalTime endTime;

        public String getClassroomId()  { return classroomId; }
        public String getPurpose()      { return purpose; }
        public LocalDate getDate()      { return date; }
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime()   { return endTime; }

        public void setClassroomId(String v)  { this.classroomId = v; }
        public void setPurpose(String v)      { this.purpose = v; }
        public void setDate(LocalDate v)      { this.date = v; }
        public void setStartTime(LocalTime v) { this.startTime = v; }
        public void setEndTime(LocalTime v)   { this.endTime = v; }
    }

    // ───────────────────────────────────────────────
    //  Response — status를 String으로 노출 (API 호환성 유지)
    // ───────────────────────────────────────────────
    @Schema(description = "예약 응답")
    public static class Response {
        private String reservationId;
        private String classroomId;
        private String classroomName;
        private String userEmail;
        private String purpose;
        private LocalDate date;
        private LocalTime startTime;
        private LocalTime endTime;
        private String status;   // Enum → String 변환해서 반환 (클라이언트 호환)

        // Reservation Entity → DTO 변환
        // classroom은 반드시 로딩된 상태여야 합니다 (Service에서 fetch join 보장)
        public static Response from(Reservation r) {
            Response dto = new Response();
            dto.reservationId = r.getReservationId();
            dto.classroomId   = r.getClassroom().getClassroomId();
            dto.classroomName = r.getClassroom().getName();
            dto.userEmail     = r.getUserEmail();
            dto.purpose       = r.getPurpose();
            dto.date          = r.getDate();
            dto.startTime     = r.getStartTime();
            dto.endTime       = r.getEndTime();
            dto.status        = r.getStatus().name();   // Enum → String
            return dto;
        }

        public String getReservationId() { return reservationId; }
        public String getClassroomId()   { return classroomId; }
        public String getClassroomName() { return classroomName; }
        public String getUserEmail()     { return userEmail; }
        public String getPurpose()       { return purpose; }
        public LocalDate getDate()       { return date; }
        public LocalTime getStartTime()  { return startTime; }
        public LocalTime getEndTime()    { return endTime; }
        public String getStatus()        { return status; }
    }
}
