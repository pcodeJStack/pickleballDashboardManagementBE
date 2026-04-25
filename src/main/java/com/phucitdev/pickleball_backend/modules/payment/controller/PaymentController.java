package com.phucitdev.pickleball_backend.modules.payment.controller;

import com.phucitdev.pickleball_backend.modules.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payos")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ✅ Webhook chính (PayOS gọi)
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("Webhook payload: " + payload);

            // TODO: verify signature (QUAN TRỌNG production)
            // paymentService.verifySignature(payload);

            paymentService.handlePayosWebhook(payload);

            // ✅ Response chuẩn JSON (PayOS thích dạng này)
            return ResponseEntity.ok(Map.of(
                    "code", "00",
                    "desc", "success"
            ));

        } catch (Exception e) {
            e.printStackTrace();

            // ❗ vẫn trả 200 nhưng báo fail trong body
            return ResponseEntity.ok(Map.of(
                    "code", "99",
                    "desc", "error"
            ));
        }
    }

    // ✅ Endpoint để PayOS verify URL (GET/HEAD)
    @RequestMapping(value = "/webhook")
    public ResponseEntity<?> verify() {
        return ResponseEntity.ok(Map.of(
                "status", "ok"
        ));
    }
}