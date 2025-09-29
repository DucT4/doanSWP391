package com.swp391.Warranty_EV.service;

import com.swp391.Warranty_EV.dto.*;
import com.swp391.Warranty_EV.entity.User;
import com.swp391.Warranty_EV.enums.Role;
import com.swp391.Warranty_EV.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // (tuỳ chọn) cấu hình secret để bảo vệ việc tạo ADMIN
    @Value("${app.admin.secret:}")   // đặt trong application.properties nếu dùng
    private String adminSecretConfig;

    private static final Set<Role> ALLOWED_ROLES =
            EnumSet.of(Role.SC_STAFF, Role.SC_TECHNICIAN, Role.SC_MANAGER,
                    Role.EVM_STAFF, Role.EVM_ADMIN);


    // ===== Register =====
    public UserResponse register(RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Lấy role người dùng chọn; nếu null -> mặc định SC_STAFF
        Role requestedRole = (req.getRole() == null) ? Role.SC_STAFF : req.getRole();

        // Chỉ chấp nhận các role hợp lệ
        if (!ALLOWED_ROLES.contains(requestedRole)) {
            throw new RuntimeException("Invalid role");
        }

        // Quy tắc “chỉ 1 EVM_ADMIN duy nhất”
        if (requestedRole == Role.EVM_ADMIN) {
            // 1) Đã có EVM_ADMIN rồi -> chặn
            if (userRepository.existsByRole(Role.EVM_ADMIN)) {
                throw new RuntimeException("EVM_ADMIN already exists");
            }

            // 2) (tuỳ chọn) yêu cầu adminSecret khớp cấu hình
            if (adminSecretConfig != null && !adminSecretConfig.isBlank()) {
                if (req.getAdminSecret() == null || !req.getAdminSecret().equals(adminSecretConfig)) {
                    throw new RuntimeException("Not allowed to create EVM_ADMIN");
                }
            }
        }


        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .role(requestedRole)
                .build();

        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    // ===== Login =====
    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = tokenService.generateToken(user);
        return new LoginResponse(
                token,
                new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name()
                )
        );
    }

    // Cho Spring Security dùng
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole().name())
                .build();
    }
}
