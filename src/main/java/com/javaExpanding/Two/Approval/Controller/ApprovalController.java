package com.javaExpanding.Two.Approval.Controller;

import com.javaExpanding.Two.Approval.Database.Approval;
import com.javaExpanding.Two.Approval.Dto.ApprovalRequestDto;
import com.javaExpanding.Two.Approval.Service.ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "승인 관리", description = "예약 승인/반려 처리 API")
@RestController
@RequestMapping("/api/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @Operation(summary = "예약 승인/반려", description = "관리자가 예약을 승인하거나 반려합니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @PostMapping("/process")
    public ResponseEntity<String> process(@Valid @RequestBody ApprovalRequestDto dto) {
        try {
            approvalService.processApproval(dto);
            String result = dto.getAppIsApprov() ? "승인" : "반려";
            return ResponseEntity.ok("예약이 " + result + " 처리되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "승인 목록 조회", description = "관리자가 전체 승인/반려 내역을 조회합니다.")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN1','ADMIN2','ADMIN3','ADMIN4','ADMIN5')")
    @GetMapping("/list")
    public ResponseEntity<?> getList(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<Approval> approvals = approvalService.getAllApprovals(page);
            return ResponseEntity.ok(approvals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("조회 중 오류가 발생했습니다.");
        }
    }
}
