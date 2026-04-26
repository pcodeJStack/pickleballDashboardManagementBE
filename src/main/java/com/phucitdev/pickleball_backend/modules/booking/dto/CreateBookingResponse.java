package com.phucitdev.pickleball_backend.modules.booking.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingResponse{
    private UUID bookingId;
    private String paymentUrl;
    private String qrCode;
    private Long orderCode;
}
