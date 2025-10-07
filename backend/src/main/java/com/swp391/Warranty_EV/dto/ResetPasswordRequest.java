package com.swp391.Warranty_EV.dto;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}
