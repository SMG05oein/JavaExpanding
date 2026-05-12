package com.javaExpanding.Two.Approval.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApprovalRequestDto {

    @NotNull(message = "예약 ID는 필수입니다.")
    private Integer resIdx;

    @NotNull(message = "승인 여부는 필수입니다.")
    private Boolean appIsApprov; // true: 승인, false: 반려

    private String appComment; // 반려 사유 또는 기타 코멘트
}
