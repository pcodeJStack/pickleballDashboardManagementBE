package com.phucitdev.pickleball_backend.modules.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String checkoutUrl;
    private String qrCode;
    private Long orderCode;
}