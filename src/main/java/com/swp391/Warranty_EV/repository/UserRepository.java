package com.swp391.Warranty_EV.repository;

import com.swp391.Warranty_EV.entity.User;
import com.swp391.Warranty_EV.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByRole(Role role);   // dùng để kiểm tra đã có ADMIN chưa
    long countByRole(Role role);       // (nếu muốn đếm)
}
