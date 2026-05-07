package com.phucitdev.pickleball_backend.modules.payment.repository;

import com.phucitdev.pickleball_backend.modules.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    boolean existsByBookingId(UUID bookingId);
    Optional<Payment> findByPayosOrderCode(Long orderCode);
    Optional<Payment> findByBookingId(UUID bookingId);

}
