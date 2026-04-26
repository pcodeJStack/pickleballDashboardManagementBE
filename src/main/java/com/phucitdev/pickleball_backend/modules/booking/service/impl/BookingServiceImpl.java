package com.phucitdev.pickleball_backend.modules.booking.service.impl;

import com.phucitdev.pickleball_backend.commo.exception.court.BadRequestException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.auth.entity.CustomerProfile;
import com.phucitdev.pickleball_backend.modules.auth.security.SecurityUtils;
import com.phucitdev.pickleball_backend.modules.booking.dto.BookingStatus;
import com.phucitdev.pickleball_backend.modules.booking.dto.CreateBookingRequest;
import com.phucitdev.pickleball_backend.modules.booking.dto.CreateBookingResponse;
import com.phucitdev.pickleball_backend.modules.booking.dto.TimeSlotResponse;
import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import com.phucitdev.pickleball_backend.modules.booking.repository.BookingRepository;
import com.phucitdev.pickleball_backend.modules.booking.service.BookingService;
import com.phucitdev.pickleball_backend.modules.court.entity.Court;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtPricing;
import com.phucitdev.pickleball_backend.modules.court.entity.TimeSlot;
import com.phucitdev.pickleball_backend.modules.court.repository.CourtPricingRepository;
import com.phucitdev.pickleball_backend.modules.court.repository.CourtRepository;
import com.phucitdev.pickleball_backend.modules.court.repository.TimeSlotRepository;
import com.phucitdev.pickleball_backend.modules.payment.dto.PaymentResponse;
import com.phucitdev.pickleball_backend.modules.payment.service.PaymentService;
import com.phucitdev.pickleball_backend.modules.payment.service.PayosService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private final CourtRepository courtRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final BookingRepository bookingRepository;
    private final CourtPricingRepository courtPricingRepository;
    private final PaymentService paymentService;
    public  BookingServiceImpl(CourtRepository courtRepository, TimeSlotRepository timeSlotRepository, BookingRepository bookingRepository, CourtPricingRepository courtPricingRepository, PaymentService  paymentService) {
        this.courtRepository = courtRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.bookingRepository = bookingRepository;
        this.courtPricingRepository = courtPricingRepository;
        this.paymentService = paymentService;
    }
    @Override
    public Page<TimeSlotResponse> getAvailableSlots(UUID courtId, LocalDate date, Pageable pageable) {
        if (date == null || date.isBefore(LocalDate.now())) {
            throw new BadRequestException("Ngày không hợp lệ!");
        }
        courtRepository.findById(courtId)
                .orElseThrow(() -> new NotFoundException("Sân không tồn tại!"));
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Page<TimeSlot> page = timeSlotRepository.findAvailableSlots(courtId, date, dayOfWeek, pageable);
        return page.map(ts -> new TimeSlotResponse(
                ts.getId(),
                ts.getStartTime(),
                ts.getEndTime()
        ));
    }
    @Override
    @Transactional
    public CreateBookingResponse createBooking(CreateBookingRequest createBookingRequest) {
        if (createBookingRequest.getBookingDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Không thể đặt ngày trong quá khứ!");
        }
        Account acc = SecurityUtils.getCurrentAccount();
        CustomerProfile cp = acc.getCustomerProfile();
        Court court = courtRepository.findById(createBookingRequest.getCourtId())
                .orElseThrow(() -> new NotFoundException("Sân không tồn tại!"));
        TimeSlot slot = timeSlotRepository.findById(createBookingRequest.getTimeSlotId())
                .orElseThrow(() -> new NotFoundException("Khung giờ không tồn tại!"));
        boolean isBooked = bookingRepository
                .existsByCourtIdAndTimeSlotIdAndBookingDate(
                        court.getId(),
                        slot.getId(),
                        createBookingRequest.getBookingDate()
                );

        if (isBooked) {
            throw new BadRequestException("Khung giờ đã được đặt! Hãy chọn khung giờ khác");
        }
        Booking booking = new Booking();
        booking.setCourt(court);
        booking.setTimeSlot(slot);
        booking.setBookingDate(createBookingRequest.getBookingDate());
        booking.setCustomerProfile(cp);
        booking.setStatus(BookingStatus.PENDING);
        CourtPricing pricing = courtPricingRepository
                .findByCourtIdAndTimeSlotId(court.getId(), slot.getId())
                .orElseThrow(() -> new NotFoundException("Chưa cấu hình giá cho khung giờ này!"));
        BigDecimal price = pricing.getPrice();
        booking.setPrice(price);
        bookingRepository.save(booking);
        PaymentResponse payment = paymentService.createPayment(booking.getId());
        return new CreateBookingResponse(
                booking.getId(),
                payment.getCheckoutUrl(),
                payment.getQrCode(),
                payment.getOrderCode());
    }
}
