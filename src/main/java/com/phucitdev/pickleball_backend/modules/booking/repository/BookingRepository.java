package com.phucitdev.pickleball_backend.modules.booking.repository;

import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    boolean existsByCourtIdAndTimeSlotIdAndBookingDate(
            UUID courtId,
            UUID timeSlotId,
            LocalDate bookingDate
    );

}
