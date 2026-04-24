package com.phucitdev.pickleball_backend.modules.zone.dto;

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
public class ZoneResponse {
    private UUID id;
    private String name;
    private String description;
    private Integer maxCourts;
    private Long currentCourts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String branchName;
}
