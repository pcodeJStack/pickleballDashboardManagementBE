package com.phucitdev.pickleball_backend.modules.payment.entity;

import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class PaymentTransaction extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    private String transactionCode;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(columnDefinition = "TEXT")
    private String rawResponse; // JSON từ PayOS

    private String failureReason;
}