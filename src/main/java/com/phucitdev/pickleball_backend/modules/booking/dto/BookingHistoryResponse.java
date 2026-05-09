package com.phucitdev.pickleball_backend.modules.booking.dto;

import com.phucitdev.pickleball_backend.modules.court.entity.CourtSurfaceType;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingHistoryResponse {
    private UUID id;
    private String courtName;

    private Integer courtNumber;
    private CourtType courtType;
    private CourtSurfaceType surfaceType;
    private String imageUrl;
    private String location;
    private Integer maxPlayers;

    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private BigDecimal totalPrice;
}
