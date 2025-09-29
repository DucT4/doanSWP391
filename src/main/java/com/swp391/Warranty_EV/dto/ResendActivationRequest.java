package com.swp391.Warranty_EV.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record ResendActivationRequest(
        @NotBlank @Email String email
) {}