package com.phucitdev.pickleball_backend.modules.court.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotItem {
    private UUID id;
    private String startTime;
    private String endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
