package com.phucitdev.pickleball_backend.modules.court.service;
import com.phucitdev.pickleball_backend.modules.court.dto.*;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtStatus;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtSurfaceType;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtType;
import org.springframework.data.domain.Pageable;
import java.util.UUID;
public interface CourtService {
    CreateNewCourtResponse createNewCourt(CreateNewCourtRequest createNewCourtRequest);
    GetAllCourtsResponse getAllCourts( UUID branchId, UUID zoneId,
                                               String name,
                                               CourtType courtType,
                                               CourtSurfaceType surfaceType,
                                               CourtStatus courtStatus,
                                               Pageable pageable);
    UpdateCourtResponse updateCourt(UUID id, UpdateCourtRequest request);
    DeleteCourtResponse deleteCourt(UUID courtId);
}
