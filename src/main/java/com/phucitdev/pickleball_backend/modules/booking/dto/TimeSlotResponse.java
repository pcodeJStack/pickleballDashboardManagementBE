package com.phucitdev.pickleball_backend.modules.booking.dto;

import java.time.LocalTime;
import java.util.UUID;

public record TimeSlotResponse(
        UUID id,
        LocalTime startTime,
        LocalTime endTime
) {}