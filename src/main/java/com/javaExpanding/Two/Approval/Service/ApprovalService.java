package com.javaExpanding.Two.Approval.Service;

import com.javaExpanding.Two.Approval.Dto.ApprovalRequestDto;
import com.javaExpanding.Two.Approval.Database.Approval;
import org.springframework.data.domain.Page;

public interface ApprovalService {
    void processApproval(ApprovalRequestDto dto);   // 승인 또는 반려 처리
    Page<Approval> getAllApprovals(int page);        // 전체 승인 목록 (관리자용)
}
