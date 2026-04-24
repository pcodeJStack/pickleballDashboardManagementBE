package com.phucitdev.pickleball_backend.modules.payment.entity;
import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import com.phucitdev.pickleball_backend.modules.payment.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String method; // PAYOS

    private String paymentUrl;

    private String currentTransactionCode; // transaction hiện tại

    private LocalDateTime paidAt;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<PaymentTransaction> transactions;
}
