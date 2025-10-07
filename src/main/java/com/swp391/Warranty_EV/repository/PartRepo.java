package com.swp391.Warranty_EV.repository;

import com.swp391.Warranty_EV.entity.Part;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepo extends JpaRepository<Part, Long> {
    Page<Part> findByPartNoContainingIgnoreCaseOrNameContainingIgnoreCase(String q1, String q2, Pageable pageable);
}
