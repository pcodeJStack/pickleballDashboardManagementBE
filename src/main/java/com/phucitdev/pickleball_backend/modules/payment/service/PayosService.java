package com.phucitdev.pickleball_backend.modules.payment.service;



import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import java.math.BigDecimal;

public interface PayosService {
    CreatePaymentLinkResponse createPaymentLink(
            Long orderCode,
            BigDecimal amount,
            String description
    ) throws Exception;
}
