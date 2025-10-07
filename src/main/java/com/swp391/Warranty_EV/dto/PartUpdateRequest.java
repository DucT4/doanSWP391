package com.swp391.Warranty_EV.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PartUpdateRequest(
        @NotBlank String name,
        @NotBlank String uom,
        Boolean trackSerial,
        Boolean trackLot
) {}

