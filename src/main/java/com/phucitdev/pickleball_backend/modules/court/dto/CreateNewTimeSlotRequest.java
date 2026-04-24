package com.phucitdev.pickleball_backend.modules.court.dto;
import lombok.Data;
import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;
@Data
public class CreateNewTimeSlotRequest {
    @NotNull(message = "Start time must not be null")
    private LocalTime startTime;

    @NotNull(message = "End time must not be null")
    private LocalTime endTime;
}