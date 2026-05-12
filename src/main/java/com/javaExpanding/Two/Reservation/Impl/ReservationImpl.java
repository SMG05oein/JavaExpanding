package com.javaExpanding.Two.Reservation.Impl;

import com.javaExpanding.Two.Facility.Database.Facility;
import com.javaExpanding.Two.Facility.Repository.FacilityRepository;
import com.javaExpanding.Two.Reservation.Database.Reservation;
import com.javaExpanding.Two.Reservation.Dto.ReservationRequestDto;
import com.javaExpanding.Two.Reservation.Repository.ReservationRepository;
import com.javaExpanding.Two.Reservation.Service.ReservationService;
import com.javaExpanding.Two.User.Database.Users;
import com.javaExpanding.Two.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    /* 예약 생성 */
    @Override
    @Transactional
    public void createReservation(ReservationRequestDto dto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = userRepository.findByUserEmail(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

        Facility facility = facilityRepository.findById(dto.getFacIdx())
                .orElseThrow(() -> new RuntimeException("시설물을 찾을 수 없습니다."));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setFacility(facility);
        reservation.setResDate(dto.getResDate());
        reservation.setResStart(dto.getResStart());
        reservation.setResEnd(dto.getResEnd());
        reservation.setResPurpose(dto.getResPurpose());
        reservation.setResHeadcount(dto.getResHeadcount());
        reservation.setResStatus(Reservation.ResStatus.대기);

        reservationRepository.save(reservation);
    }

    /* 예약 수정 */
    @Override
    @Transactional
    public void updateReservation(Integer resIdx, ReservationRequestDto dto) {
        Reservation reservation = reservationRepository.findById(resIdx)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        Facility facility = facilityRepository.findById(dto.getFacIdx())
                .orElseThrow(() -> new RuntimeException("시설물을 찾을 수 없습니다."));

        reservation.setFacility(facility);
        reservation.setResDate(dto.getResDate());
        reservation.setResStart(dto.getResStart());
        reservation.setResEnd(dto.getResEnd());
        reservation.setResPurpose(dto.getResPurpose());
        reservation.setResHeadcount(dto.getResHeadcount());
    }

    /* 예약 삭제 (관리자용) */
    @Override
    @Transactional
    public void deleteReservation(Integer resIdx) {
        Reservation reservation = reservationRepository.findById(resIdx)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));
        reservationRepository.delete(reservation);
    }

    /* 예약 취소 (유저용) */
    @Override
    @Transactional
    public void cancelReservation(Integer resIdx) {
        Reservation reservation = reservationRepository.findById(resIdx)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));
        reservation.setResStatus(Reservation.ResStatus.취소);
    }

    /* 전체 예약 목록 - 관리자용 */
    @Override
    @Transactional(readOnly = true)
    public Page<Reservation> getAllReservations(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("resIdx").descending());
        return reservationRepository.findAll(pageRequest);
    }

    /* 내 예약 목록 - 유저용 */
    @Override
    @Transactional(readOnly = true)
    public Page<Reservation> getMyReservations(int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = userRepository.findByUserEmail(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("resIdx").descending());
        return reservationRepository.findByUser(user, pageRequest);
    }
}
