package com.phucitdev.pickleball_backend.modules.branch.dto;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record BranchResponse(
        UUID id,
        String name,
        String address,
        String phone,
        String imageUrl,
        String description,
        LocalTime openTime,
        LocalTime closeTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}