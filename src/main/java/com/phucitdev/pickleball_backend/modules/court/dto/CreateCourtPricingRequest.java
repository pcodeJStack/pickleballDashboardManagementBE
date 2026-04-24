package com.phucitdev.pickleball_backend.modules.court.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourtPricingRequest {
    @NotNull(message = "CourtId must not be null")
    private UUID courtId;

    @NotNull(message = "TimeSlotId must not be null")
    private UUID timeSlotId;

    @NotNull(message = "DayOfWeek must not be null")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
}
