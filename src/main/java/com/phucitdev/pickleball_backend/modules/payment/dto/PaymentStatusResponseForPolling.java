package com.phucitdev.pickleball_backend.modules.payment.dto;
import com.phucitdev.pickleball_backend.modules.payment.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusResponseForPolling {
    private PaymentStatus status;
    private UUID bookingId;
    private BigDecimal amount;
}
