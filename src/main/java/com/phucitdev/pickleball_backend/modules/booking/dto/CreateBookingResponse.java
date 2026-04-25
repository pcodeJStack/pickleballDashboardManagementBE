package com.phucitdev.pickleball_backend.modules.booking.dto;


import java.util.UUID;

public record CreateBookingResponse(
        UUID bookingId,
        String paymentUrl,
        String qrCode,
        Long orderCode
) {}