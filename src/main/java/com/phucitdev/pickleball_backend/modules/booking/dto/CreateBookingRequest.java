package com.phucitdev.pickleball_backend.modules.booking.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest{
    @NotNull(message = "CourtId không được để trống")
   private UUID courtId;
    @NotNull(message = "TimeSlotId không được để trống")
    private UUID timeSlotId;
    @NotNull(message = "Ngày đặt không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookingDate;
}
