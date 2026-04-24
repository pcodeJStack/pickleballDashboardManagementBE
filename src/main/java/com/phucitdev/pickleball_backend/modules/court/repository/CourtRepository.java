package com.phucitdev.pickleball_backend.modules.court.repository;
import com.phucitdev.pickleball_backend.modules.court.dto.CourtResponse;
import com.phucitdev.pickleball_backend.modules.court.entity.Court;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtStatus;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtSurfaceType;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
public interface CourtRepository extends JpaRepository<Court, UUID> {
    @Query("""
SELECT new com.phucitdev.pickleball_backend.modules.court.dto.CourtResponse(
    c.id,
    c.name,
    c.description,
    c.courtNumber,
    c.courtType,
    c.surfaceType,
    c.courtStatus,
    c.imageUrl,
    c.location,
    c.maxPlayers,
    c.createdAt,
    c.updatedAt,
    c.zone.id,
    c.zone.name
)
FROM Court c
WHERE c.isDeleted = false
AND (:name IS NULL OR c.name LIKE CONCAT(:name, '%'))
AND (:courtType IS NULL OR c.courtType = :courtType)
AND (:surfaceType IS NULL OR c.surfaceType = :surfaceType)
AND (:courtStatus IS NULL OR c.courtStatus = :courtStatus)
AND (:branchId IS NULL OR c.zone.branch.id = :branchId)
AND (:zoneId IS NULL OR c.zone.id = :zoneId)
""")
    Page<CourtResponse> search(
            @Param("name") String name,
            @Param("courtType") CourtType courtType,
            @Param("surfaceType") CourtSurfaceType surfaceType,
            @Param("courtStatus") CourtStatus courtStatus,
            @Param("branchId") UUID branchId,
            @Param("zoneId") UUID zoneId,
            Pageable pageable
    );
   boolean existsByZoneIdAndCourtNumber(UUID zoneId, Integer courtNumber);
   Optional<Court> findByIdAndIsDeletedFalse(UUID courtId);
}
