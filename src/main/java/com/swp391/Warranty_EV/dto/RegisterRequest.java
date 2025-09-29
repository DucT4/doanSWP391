package com.swp391.Warranty_EV.dto;

import com.swp391.Warranty_EV.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    private Role role; // optional (nếu null -> mặc định CUSTOMER)
    private String adminSecret;
}