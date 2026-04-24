package com.phucitdev.pickleball_backend.modules.booking.dto;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.UUID;
public record CreateBookingRequest(
        @NotNull(message = "CourtId không được để trống")
        UUID courtId,
        @NotNull(message = "TimeSlotId không được để trống")
        UUID timeSlotId,
        @NotNull(message = "Ngày đặt không được để trống")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate bookingDate
) {}