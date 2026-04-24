package com.phucitdev.pickleball_backend.modules.booking.dto;

import java.time.LocalTime;

public record TimeSlotResponse(
        Long id,
        LocalTime startTime,
        LocalTime endTime
) {}