package com.phucitdev.pickleball_backend.modules.court.repository;
import com.phucitdev.pickleball_backend.modules.court.dto.GetAllCourtPricingsResponse;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtPricing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;
public interface CourtPricingRepository extends JpaRepository<CourtPricing, UUID> {
    boolean existsByCourtIdAndTimeSlotIdAndDayOfWeek(
            UUID courtId,
            UUID timeSlotId,
            DayOfWeek dayOfWeek
    );
    @Query("""
SELECT new com.phucitdev.pickleball_backend.modules.court.dto.GetAllCourtPricingsResponse$Item(
    cp.id,
    c.name,
    c.id,
    z.name,
    z.id,
    b.name,
    cp.dayOfWeek,
    ts.startTime,
    ts.endTime,
    cp.price
)
FROM CourtPricing cp
JOIN cp.court c
JOIN c.zone z
JOIN z.branch b
JOIN cp.timeSlot ts
WHERE (:branchId IS NULL OR b.id = :branchId)
AND (:zoneId IS NULL OR z.id = :zoneId)
AND (:courtId IS NULL OR c.id = :courtId)
AND (:dayOfWeek IS NULL OR cp.dayOfWeek = :dayOfWeek)
AND (:startTime IS NULL OR ts.startTime >= :startTime)
AND (:endTime IS NULL OR ts.endTime <= :endTime)
""")
    Page<GetAllCourtPricingsResponse.Item> search(
            @Param("branchId") UUID branchId,
            @Param("zoneId") UUID zoneId,
            @Param("courtId") UUID courtId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            Pageable pageable
    );
}
