package com.phucitdev.pickleball_backend.modules.payment.controller;

import com.phucitdev.pickleball_backend.modules.payment.dto.PaymentStatusResponseForPolling;
import com.phucitdev.pickleball_backend.modules.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
@RequestMapping("/api")
public class PollingAPI {
    private final PaymentService paymentService;
    public PollingAPI(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @GetMapping("/payment/{orderCode}/status")
    public ResponseEntity<PaymentStatusResponseForPolling> getStatus(@PathVariable Long orderCode) {
        PaymentStatusResponseForPolling response = paymentService.checkingPaymentStatus(orderCode);
        return ResponseEntity.ok(response);
    }
}
