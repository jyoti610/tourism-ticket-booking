package com.college.tourism.dto.LoginDTO;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String email;
    private String otp;
    private String newPassword;
}
