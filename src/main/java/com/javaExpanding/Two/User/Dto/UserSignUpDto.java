package com.javaExpanding.Two.User.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {
    private String userEmail;
    private String userPw;
    private String userName;
}