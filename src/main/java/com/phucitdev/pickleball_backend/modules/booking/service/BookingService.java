package com.phucitdev.pickleball_backend.modules.booking.service;
import com.phucitdev.pickleball_backend.modules.booking.dto.CreateBookingRequest;
import com.phucitdev.pickleball_backend.modules.booking.dto.CreateBookingResponse;
import com.phucitdev.pickleball_backend.modules.booking.dto.TimeSlotResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.UUID;
public interface BookingService {
    Page<TimeSlotResponse> getAvailableSlots(UUID courtId, LocalDate date, Pageable pageable);
    CreateBookingResponse createBooking(CreateBookingRequest createBookingRequest);
}
