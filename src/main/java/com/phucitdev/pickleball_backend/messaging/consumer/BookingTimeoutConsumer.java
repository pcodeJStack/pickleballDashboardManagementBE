package com.phucitdev.pickleball_backend.messaging.consumer;
import com.phucitdev.pickleball_backend.messaging.config.RabbitConfig;
import com.phucitdev.pickleball_backend.modules.booking.dto.BookingStatus;
import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import com.phucitdev.pickleball_backend.modules.booking.repository.BookingRepository;
import com.phucitdev.pickleball_backend.modules.payment.entity.Payment;
import com.phucitdev.pickleball_backend.modules.payment.entity.PaymentStatus;
import com.phucitdev.pickleball_backend.modules.payment.repository.PaymentRepository;
import com.phucitdev.pickleball_backend.modules.payment.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class BookingTimeoutConsumer {
    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    @RabbitListener(queues = RabbitConfig.BOOKING_QUEUE)
    @Transactional
    public void handleTimeout(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null);
        if (booking == null) return;
        if (booking.getStatus() != BookingStatus.PENDING) return;

        // atomic
        int updated = bookingRepository.updateStatusIfMatch(
                bookingId,
                BookingStatus.PENDING,
                BookingStatus.CANCELLED,
                bookingId.toString()
        );

        if (updated == 1) {
            Payment payment = paymentRepository.findByBookingId(bookingId)
                    .orElse(null);
            if (payment != null && payment.getStatus() != PaymentStatus.SUCCESS) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
            }
        }
    }
}