package com.javaExpanding.Two.Reservation.Service;

import com.javaExpanding.Two.Reservation.Dto.ReservationRequestDto;
import com.javaExpanding.Two.Reservation.Database.Reservation;
import org.springframework.data.domain.Page;

public interface ReservationService {
    void createReservation(ReservationRequestDto dto);       // 예약 생성
    void updateReservation(Integer resIdx, ReservationRequestDto dto); // 예약 수정
    void deleteReservation(Integer resIdx);                  // 예약 삭제
    void cancelReservation(Integer resIdx);                  // 예약 취소
    Page<Reservation> getAllReservations(int page);          // 전체 목록 (관리자용)
    Page<Reservation> getMyReservations(int page);          // 내 예약 목록 (유저용)
}
