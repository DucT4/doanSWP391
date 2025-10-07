package com.swp391.Warranty_EV.controller;
import com.swp391.Warranty_EV.dto.PartResponse;
import com.swp391.Warranty_EV.dto.PartUpdateRequest;
import com.swp391.Warranty_EV.service.TechnicianProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@SecurityRequirement(name = "api")
@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping("/api/tech/products")
@RequiredArgsConstructor
public class TechnicianProductController {

    private final TechnicianProductService service;

    // Implement API getAllProduct
    @GetMapping
    public ResponseEntity<Page<PartResponse>> getAll(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String dir
    ) {
        return ResponseEntity.ok(service.getAll(q, page, size, sortBy, dir));
    }

    // Implement API update product
    @PutMapping("/{id}")
    public ResponseEntity<PartResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid PartUpdateRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    // Implement API delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}