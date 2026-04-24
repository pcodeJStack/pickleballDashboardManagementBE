package com.phucitdev.pickleball_backend.modules.zone.service;

import com.phucitdev.pickleball_backend.modules.zone.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ZoneService {
    CreateNewZoneResponse createNewZone(CreateNewZoneRequest createNewZoneRequest);
    GetAllZonesResponse getAllZones(UUID branchId, String name, Pageable  pageable);
    UpdateZoneResponse updateZone(UUID id, UpdateZoneRequest updateZoneRequest);
    DeleteZoneResponse deleteZone(UUID id);
}
