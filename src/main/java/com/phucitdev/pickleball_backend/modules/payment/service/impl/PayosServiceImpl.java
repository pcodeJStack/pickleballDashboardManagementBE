package com.phucitdev.pickleball_backend.modules.payment.service.impl;


import com.phucitdev.pickleball_backend.modules.payment.service.PayosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import java.math.BigDecimal;

@Service
@Slf4j
public class PayosServiceImpl implements PayosService {
    private final PayOS payOS;
    public PayosServiceImpl(PayOS payOS) {
        this.payOS = payOS;
    }
    @Override
    public CreatePaymentLinkResponse createPaymentLink(Long orderCode, BigDecimal amount, String description) throws Exception {
        long finalAmount = amount.longValue();
        log.info("Create PayOS payment: orderCode={}, amount={}", orderCode, finalAmount);
        PaymentLinkItem item = PaymentLinkItem.builder()
                .name("Booking #" + orderCode)
                .price(finalAmount)
                .quantity(1)
                .build();
        CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(finalAmount)
                .description(description)
                .item(item)
                .returnUrl("https://your-frontend.com/payment-success")
                .cancelUrl("https://your-frontend.com/payment-cancel")
                .expiredAt((System.currentTimeMillis() / 1000) + 900) // 15 phút
                .build();

        return payOS.paymentRequests().create(request);
    }
}
