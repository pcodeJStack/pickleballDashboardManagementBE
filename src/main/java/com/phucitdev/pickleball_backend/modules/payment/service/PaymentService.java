package com.phucitdev.pickleball_backend.modules.payment.service;
import com.phucitdev.pickleball_backend.modules.payment.dto.PaymentResponse;

import java.util.Map;
import java.util.UUID;

public interface PaymentService {
    PaymentResponse createPayment(UUID bookingId);
    void handlePayosWebhook(Map<String, Object> payload);
}
