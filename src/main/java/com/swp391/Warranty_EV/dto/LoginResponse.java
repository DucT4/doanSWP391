package com.swp391.Warranty_EV.dto;

import com.swp391.Warranty_EV.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserResponse user;
}
