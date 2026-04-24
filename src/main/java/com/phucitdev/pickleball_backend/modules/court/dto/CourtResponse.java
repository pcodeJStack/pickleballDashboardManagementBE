package com.phucitdev.pickleball_backend.modules.court.dto;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtStatus;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtSurfaceType;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtType;
import java.time.LocalDateTime;
import java.util.UUID;

public record CourtResponse(
        UUID id,
        String name,
        String description,
        Integer courtNumber,
        CourtType courtType,
        CourtSurfaceType surfaceType,
        CourtStatus courtStatus,
        String imageUrl,
        String location,
        Integer maxPlayers,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID zoneId,
        String zoneName
) {}

