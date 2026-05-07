package com.phucitdev.pickleball_backend.modules.payment.service.impl;
import com.phucitdev.pickleball_backend.commo.exception.court.DuplicateResourceException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.modules.booking.dto.BookingStatus;
import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import com.phucitdev.pickleball_backend.modules.booking.repository.BookingRepository;
import com.phucitdev.pickleball_backend.modules.payment.dto.PaymentResponse;
import com.phucitdev.pickleball_backend.modules.payment.dto.PaymentStatusResponseForPolling;
import com.phucitdev.pickleball_backend.modules.payment.entity.Payment;
import com.phucitdev.pickleball_backend.modules.payment.entity.PaymentMethod;
import com.phucitdev.pickleball_backend.modules.payment.entity.PaymentStatus;
import com.phucitdev.pickleball_backend.modules.payment.entity.PaymentTransaction;
import com.phucitdev.pickleball_backend.modules.payment.repository.PaymentRepository;
import com.phucitdev.pickleball_backend.modules.payment.service.PaymentService;
import com.phucitdev.pickleball_backend.modules.payment.service.PayosService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
public class PaymentServiceImpl implements PaymentService {
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final PayosService payosService;
    private final PayOS payOS;
    public PaymentServiceImpl(BookingRepository bookingRepository, PaymentRepository paymentRepository, PayosService payosService, PayOS payOS) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.payosService = payosService;
        this.payOS = payOS;
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

            PaymentTransaction paymentTransaction = new PaymentTransaction();
            paymentTransaction.setPayment(payment);
            paymentTransaction.setTransactionCode(String.valueOf(orderCode));
            paymentTransaction.setAmount(payment.getAmount());
            paymentTransaction.setStatus(PaymentStatus.PENDING);
            paymentTransaction.setRawResponse(res.toString());
            payment.setTransactions(List.of(paymentTransaction));
            payment.setCurrentTransactionCode(paymentTransaction.getTransactionCode());
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

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }
        Booking booking = payment.getBooking();
        if ("00".equals(code)) {
            int updated = bookingRepository.updateStatusIfMatch(
                    booking.getId(),
                    BookingStatus.PENDING,
                    BookingStatus.CONFIRMED,
                    "ACTIVE"
            );
            if (updated == 1) {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setPaidAt(LocalDateTime.now());
            } else {
                // 👉 booking đã bị CANCEL trước đó
                // tuỳ business:
                // - vẫn set SUCCESS
                // - hoặc log để xử lý hoàn tiền
                payment.setStatus(PaymentStatus.SUCCESS);
            }

        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setCancelReason("Thanh toán thất bại");
        }
        paymentRepository.save(payment);
    }

    @Override
    public PaymentStatusResponseForPolling checkingPaymentStatus(Long orderCode) {
        Payment payment = paymentRepository.findByPayosOrderCode(orderCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy payment"));
        return new PaymentStatusResponseForPolling(
                payment.getStatus(),
                payment.getBooking().getId(),
                payment.getAmount()
        );
    }


}
