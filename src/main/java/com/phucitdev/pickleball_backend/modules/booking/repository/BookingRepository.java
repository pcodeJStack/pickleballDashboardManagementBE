package com.phucitdev.pickleball_backend.modules.booking.repository;

import com.phucitdev.pickleball_backend.modules.booking.dto.BookingStatus;
import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    boolean existsByCourtIdAndTimeSlotIdAndBookingDateAndStatus(
            UUID courtId,
            UUID timeSlotId,
            LocalDate bookingDate,
            BookingStatus status
    );
    @Modifying
    @Query("""
    UPDATE Booking b
    SET b.status = :newStatus,
    b.active = :active
    WHERE b.id = :id AND b.status = :currentStatus
""")
    int updateStatusIfMatch(UUID id, BookingStatus currentStatus, BookingStatus newStatus, String active);
}
