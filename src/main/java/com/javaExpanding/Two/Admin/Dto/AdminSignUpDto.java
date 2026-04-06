package com.javaExpanding.Two.Admin.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminSignUpDto {
    private String adminId;
    private String adminEmail;
    private String adminPw;
    private String adminName;
}
