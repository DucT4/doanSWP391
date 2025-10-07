package com.swp391.Warranty_EV.controller;

import com.swp391.Warranty_EV.dto.AddPartRequest;
import com.swp391.Warranty_EV.dto.MessageResponse;
import com.swp391.Warranty_EV.dto.PartResponse;
import com.swp391.Warranty_EV.service.PartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PartController {

    private final PartService partService;

    @PostMapping
    public ResponseEntity<?> addPart(@Valid @RequestBody AddPartRequest request) {
        try {
            PartResponse partResponse = partService.addPart(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(partResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Error: An unexpected error occurred"));
        }
