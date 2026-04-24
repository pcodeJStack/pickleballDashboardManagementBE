package com.phucitdev.pickleball_backend.modules.court.service;

import com.phucitdev.pickleball_backend.modules.court.dto.CreateCourtPricingRequest;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateCourtPricingResponse;
import com.phucitdev.pickleball_backend.modules.court.dto.GetAllCourtPricingsResponse;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public interface CourtPricingService {
    CreateCourtPricingResponse createCourtPricing(CreateCourtPricingRequest createCourtPricingRequest);
    GetAllCourtPricingsResponse getAll(UUID branchId, UUID zoneId, UUID courtId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Pageable pageable);
}
