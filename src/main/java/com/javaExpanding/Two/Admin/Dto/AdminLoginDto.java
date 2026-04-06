package com.javaExpanding.Two.Admin.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginDto {
    private String userIdorEmail;
    private String userPw;
}
