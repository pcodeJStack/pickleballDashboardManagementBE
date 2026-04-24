package com.phucitdev.pickleball_backend.modules.zone.repository;

import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import com.phucitdev.pickleball_backend.modules.zone.dto.ZoneResponse;
import com.phucitdev.pickleball_backend.modules.zone.entity.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ZoneRepository extends JpaRepository<Zone, UUID> {
    boolean existsByBranchIdAndName(UUID branchId, String name);
    @Query("""
    SELECT new com.phucitdev.pickleball_backend.modules.zone.dto.ZoneResponse(
        z.id,
        z.name,
        z.description,
        z.maxCourts,
        COUNT(c),
        z.createdAt,
        z.updatedAt,
        z.branch.name
    )
    FROM Zone z
    LEFT JOIN z.courts c
    WHERE z.branch.id = :branchId
      AND z.isDeleted = false
      AND (:name IS NULL OR z.name LIKE CONCAT(:name, '%'))
    GROUP BY z.id, z.name, z.description, z.maxCourts, z.createdAt, z.updatedAt, z.branch.name
""")
    Page<ZoneResponse> searchByBranch(
            @Param("branchId") UUID branchId,
            @Param("name") String name,
            Pageable pageable
    );
    @Query("""
    SELECT new com.phucitdev.pickleball_backend.modules.zone.dto.ZoneResponse(
        z.id,
        z.name,
        z.description,
        z.maxCourts,
        COUNT(c),
        z.createdAt,
        z.updatedAt,
        z.branch.name
    )
    FROM Zone z
    LEFT JOIN z.courts c
    WHERE z.isDeleted = false
    AND (:name IS NULL OR z.name LIKE CONCAT(:name, '%'))
    GROUP BY z.id, z.name, z.description, z.maxCourts, z.createdAt, z.updatedAt, z.branch.name
""")
    Page<ZoneResponse> searchAll(
            @Param("name") String name,
            Pageable pageable
    );
    Optional<Zone> findByIdAndIsDeletedFalse(UUID zoneId);

}
