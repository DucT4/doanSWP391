package com.swp391.Warranty_EV.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddPartRequest {

    @NotBlank(message = "Part number is required")
    @Size(max = 64, message = "Part number must not exceed 64 characters")
    private String partNo;

    @NotBlank(message = "Part name is required")
    @Size(max = 150, message = "Part name must not exceed 150 characters")
    private String name;

    private Boolean trackSerial = false;

    private Boolean trackLot = false;

    @Size(max = 20, message = "Unit of measure must not exceed 20 characters")
    private String uom = "EA";
}
