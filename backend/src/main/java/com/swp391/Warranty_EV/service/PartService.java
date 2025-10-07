package com.swp391.Warranty_EV.service;

import com.swp391.Warranty_EV.dto.AddPartRequest;
import com.swp391.Warranty_EV.dto.PartResponse;
import com.swp391.Warranty_EV.entity.Part;
import com.swp391.Warranty_EV.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PartService {

    private final PartRepository partRepository;

    public PartResponse addPart(AddPartRequest request) {
        // Check if part number already exists
        if (partRepository.existsByPartNo(request.getPartNo())) {
            throw new RuntimeException("Part number already exists: " + request.getPartNo());
        }

        // Create new part
        Part part = new Part();
        part.setPartNo(request.getPartNo());
        part.setName(request.getName());
        part.setTrackSerial(request.getTrackSerial());
        part.setTrackLot(request.getTrackLot());
        part.setUom(request.getUom());

        // Save part
        Part savedPart = partRepository.save(part);

        // Convert to response DTO
        return convertToResponse(savedPart);
    }

    private PartResponse convertToResponse(Part part) {
        return new PartResponse(
            part.getId(),
            part.getPartNo(),
            part.getName(),
            part.getTrackSerial(),
            part.getTrackLot(),
            part.getUom()
        );
    }
}
