package com.swp391.Warranty_EV.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
