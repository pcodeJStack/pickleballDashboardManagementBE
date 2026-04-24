package com.phucitdev.pickleball_backend.modules.court.dto;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record GetAllCourtPricingsResponse(
        List<Item> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public record Item(
            UUID id,
            String courtName,
            UUID courtId,
            String zoneName,
            UUID zoneId,
            String branchName,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            BigDecimal price
    ) {}
}