package com.swp391.Warranty_EV.service;


import com.swp391.Warranty_EV.dto.PartResponse;
import com.swp391.Warranty_EV.dto.PartUpdateRequest;
import com.swp391.Warranty_EV.entity.Part;
import com.swp391.Warranty_EV.repository.PartRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TechnicianProductService {

    private final PartRepo partRepo;

    // GET all product (có tìm kiếm + phân trang + sắp xếp)
    @Transactional(readOnly = true)
    public Page<PartResponse> getAll(String q, int page, int size, String sortBy, String dir) {
        Sort sort = Sort.by("asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC,
                (sortBy == null || sortBy.isBlank()) ? "id" : sortBy);
        Pageable pageable = PageRequest.of(Math.max(page,0), Math.min(Math.max(size,1), 200), sort);

        Page<Part> pg = (q != null && !q.isBlank())
                ? partRepo.findByPartNoContainingIgnoreCaseOrNameContainingIgnoreCase(q, q, pageable)
                : partRepo.findAll(pageable);

        return pg.map(p -> new PartResponse(p.getId(), p.getPartNo(), p.getName(), p.getTrackSerial(), p.getTrackLot(), p.getUom()));
    }

    // UPDATE product
    @Transactional
    public PartResponse update(Long id, PartUpdateRequest req) {
        Part p = partRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Part không tồn tại"));
        p.setName(req.name());
        p.setUom(req.uom());
        if (req.trackSerial() != null) p.setTrackSerial(req.trackSerial());
        if (req.trackLot() != null) p.setTrackLot(req.trackLot());
        partRepo.save(p);
        return new PartResponse(p.getId(), p.getPartNo(), p.getName(), p.getTrackSerial(), p.getTrackLot(), p.getUom());
    }

    // DELETE product (sẽ lỗi nếu đang bị FK tham chiếu)
    @Transactional
    public void delete(Long id) {
        Part p = partRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Part không tồn tại"));
        try {
            partRepo.delete(p);
            partRepo.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("Không thể xoá do đang được sử dụng trong các nghiệp vụ khác", ex);
        }
    }
}