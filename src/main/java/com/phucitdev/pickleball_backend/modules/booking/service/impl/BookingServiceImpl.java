package com.phucitdev.pickleball_backend.modules.booking.service.impl;

import com.phucitdev.pickleball_backend.commo.exception.court.BadRequestException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.auth.entity.CustomerProfile;
import com.phucitdev.pickleball_backend.modules.auth.security.SecurityUtils;
import com.phucitdev.pickleball_backend.modules.booking.dto.CreateBookingRequest;
import com.phucitdev.pickleball_backend.modules.booking.dto.CreateBookingResponse;
import com.phucitdev.pickleball_backend.modules.booking.dto.TimeSlotResponse;
import com.phucitdev.pickleball_backend.modules.booking.service.BookingService;
import com.phucitdev.pickleball_backend.modules.court.entity.Court;
import com.phucitdev.pickleball_backend.modules.court.entity.TimeSlot;
import com.phucitdev.pickleball_backend.modules.court.repository.CourtRepository;
import com.phucitdev.pickleball_backend.modules.court.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    private final CourtRepository courtRepository;
    private final TimeSlotRepository timeSlotRepository;
    public  BookingServiceImpl(CourtRepository courtRepository,  TimeSlotRepository timeSlotRepository) {
        this.courtRepository = courtRepository;
        this.timeSlotRepository = timeSlotRepository;
    }


    @Override
    public Page<TimeSlotResponse> getAvailableSlots(UUID courtId, LocalDate date, Pageable pageable) {
        if (date == null || date.isBefore(LocalDate.now())) {
            throw new BadRequestException("Ngày không hợp lệ!");
        }
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new NotFoundException("Sân không tồn tại!"));
        Page<TimeSlot> page = timeSlotRepository.findAvailableSlots(courtId, date, pageable);
    }

    @Override
    @Transactional
    public CreateBookingResponse createBooking(CreateBookingRequest createBookingRequest) {
        // 1. Validate ngày
        if (createBookingRequest.bookingDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Không thể đặt ngày trong quá khứ!");
        }
        Account acc = SecurityUtils.getCurrentAccount();
        CustomerProfile cp = acc.getCustomerProfile();

        // check court có tồn tại không
        Court court = courtRepository.findById(createBookingRequest.courtId())
                .orElseThrow(() -> new NotFoundException("Sân không tồn tại!"));
        TimeSlot slot = timeSlotRepository.findById(createBookingRequest.timeSlotId())
                .orElseThrow(() -> new NotFoundException("Khung giờ không tồn tại!"));

    }
}
