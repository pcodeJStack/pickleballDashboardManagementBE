package com.phucitdev.pickleball_backend.modules.payment.service.impl;
import com.phucitdev.pickleball_backend.commo.exception.court.DuplicateResourceException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.modules.booking.dto.BookingStatus;
import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import com.phucitdev.pickleball_backend.modules.booking.repository.BookingRepository;
import com.phucitdev.pickleball_backend.modules.payment.dto.PaymentResponse;
import com.phucitdev.pickleball_backend.modules.payment.entity.Payment;
import com.phucitdev.pickleball_backend.modules.payment.entity.PaymentMethod;
import com.phucitdev.pickleball_backend.modules.payment.entity.PaymentStatus;
import com.phucitdev.pickleball_backend.modules.payment.repository.PaymentRepository;
import com.phucitdev.pickleball_backend.modules.payment.service.PaymentService;
import com.phucitdev.pickleball_backend.modules.payment.service.PayosService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@Service
public class PaymentServiceImpl implements PaymentService {
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PayosService payosService;
    public PaymentServiceImpl(BookingRepository bookingRepository, PaymentRepository paymentRepository, PayosService payosService) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.payosService = payosService;
    }
    @Override
    public PaymentResponse  createPayment(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (paymentRepository.existsByBookingId(bookingId)) {
            throw new DuplicateResourceException("Booking đã có payment");
        }
        try {
            //  unique orderCode
            Long orderCode = System.currentTimeMillis();
            var res = payosService.createPaymentLink(
                    orderCode,
                    booking.getPrice(),
                    "Thanh toán booking"
            );
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(booking.getPrice());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setPayosOrderCode(orderCode);
            payment.setPaymentUrl(res.getCheckoutUrl());
            payment.setMethod(PaymentMethod.PAYOS);
            payment.setDescription("Thanh toán booking #" + booking.getId());
            payment.setExpiredAt(LocalDateTime.now().plusMinutes(15));
            payment.setPaidAt(null);
            payment.setCancelReason(null);
            payment.setCurrentTransactionCode(null);
            paymentRepository.save(payment);
            return new PaymentResponse(
                    res.getCheckoutUrl(),
                    res.getQrCode(),
                    orderCode
            );
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo payment", e);
        }
    }
    @Override
    @Transactional
    public void handlePayosWebhook(Map<String, Object> payload) {
        if (payload == null) return;

        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        if (data == null) return;

        Long orderCode = Long.valueOf(data.get("orderCode").toString());
        String code = (String) data.get("code");

        Payment payment = paymentRepository
                .findByPayosOrderCode(orderCode)
                .orElseThrow(() -> new NotFoundException("Payment not found"));
        // tránh xử lý lại
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }
        if ("00".equals(code)) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            Booking booking = payment.getBooking();
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setCancelReason("Thanh toán thất bại");
        }

        paymentRepository.save(payment);
    }
}
