package com.javaExpanding.Two.Approval.Impl;

import com.javaExpanding.Two.Admin.Database.Admin;
import com.javaExpanding.Two.Admin.Repository.AdminRepository;
import com.javaExpanding.Two.Approval.Database.Approval;
import com.javaExpanding.Two.Approval.Dto.ApprovalRequestDto;
import com.javaExpanding.Two.Approval.Repository.ApprovalRepository;
import com.javaExpanding.Two.Approval.Service.ApprovalService;
import com.javaExpanding.Two.Reservation.Database.Reservation;
import com.javaExpanding.Two.Reservation.Repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApprovalImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ReservationRepository reservationRepository;
    private final AdminRepository adminRepository;

    /* 승인 또는 반려 처리 */
    @Override
    @Transactional
    public void processApproval(ApprovalRequestDto dto) {
        String adminId = SecurityContextHolder.getContext().getAuthentication().getName();

        Admin admin = adminRepository.findByAdminIdOrAdminEmail(adminId, adminId)
                .orElseThrow(() -> new RuntimeException("관리자 정보를 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.findById(dto.getResIdx())
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // approval 테이블에 기록
        Approval approval = new Approval();
        approval.setReservation(reservation);
        approval.setAdmin(admin);
        approval.setAppIsApprov(dto.getAppIsApprov());
        approval.setAppComment(dto.getAppComment());
        approvalRepository.save(approval);

        // reservation 상태 동기화
        if (dto.getAppIsApprov()) {
            reservation.setResStatus(Reservation.ResStatus.승인);
        } else {
            reservation.setResStatus(Reservation.ResStatus.거절);
        }
    }

    /* 전체 승인 목록 - 관리자용 */
    @Override
    @Transactional(readOnly = true)
    public Page<Approval> getAllApprovals(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("appIdx").descending());
        return approvalRepository.findAll(pageRequest);
    }
}
