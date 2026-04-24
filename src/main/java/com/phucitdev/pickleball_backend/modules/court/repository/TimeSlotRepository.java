package com.phucitdev.pickleball_backend.modules.court.repository;
import com.phucitdev.pickleball_backend.modules.court.entity.TimeSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {
    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END
    FROM TimeSlot t
    WHERE t.startTime < :endTime
      AND t.endTime > :startTime
    """)
    boolean existsOverlapping(LocalTime startTime, LocalTime endTime);
    Page<TimeSlot> findByIsDeletedFalse(Pageable pageable);

    @Query("""
    SELECT ts FROM CourtPricing cp
    JOIN cp.timeSlot ts
    WHERE cp.court.id = :courtId
    AND ts.isDeleted = false
    AND cp.dayOfWeek = :dayOfWeek
    AND NOT EXISTS (
        SELECT 1 FROM Booking b
        WHERE b.court.id = :courtId
        AND b.bookingDate = :date
        AND b.timeSlot = ts
    )
""")
    Page<TimeSlot> findAvailableSlots(
            UUID courtId,
            LocalDate date,
            DayOfWeek dayOfWeek,
            Pageable pageable
    );
}
