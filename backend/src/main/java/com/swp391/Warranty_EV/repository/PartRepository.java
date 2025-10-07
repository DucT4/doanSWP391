package com.swp391.Warranty_EV.repository;

import com.swp391.Warranty_EV.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    Optional<Part> findByPartNo(String partNo);

    boolean existsByPartNo(String partNo);
}
