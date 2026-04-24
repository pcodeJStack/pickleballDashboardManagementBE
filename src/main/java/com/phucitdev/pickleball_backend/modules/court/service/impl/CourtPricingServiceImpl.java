package com.phucitdev.pickleball_backend.modules.court.service.impl;
import com.phucitdev.pickleball_backend.commo.exception.court.BadRequestException;
import com.phucitdev.pickleball_backend.commo.exception.court.DuplicateResourceException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateCourtPricingRequest;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateCourtPricingResponse;
import com.phucitdev.pickleball_backend.modules.court.dto.GetAllCourtPricingsResponse;
import com.phucitdev.pickleball_backend.modules.court.entity.Court;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtPricing;
import com.phucitdev.pickleball_backend.modules.court.entity.TimeSlot;
import com.phucitdev.pickleball_backend.modules.court.repository.CourtPricingRepository;
import com.phucitdev.pickleball_backend.modules.court.repository.CourtRepository;
import com.phucitdev.pickleball_backend.modules.court.repository.TimeSlotRepository;
import com.phucitdev.pickleball_backend.modules.court.service.CourtPricingService;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class CourtPricingServiceImpl implements CourtPricingService {
    private final CourtPricingRepository courtPricingRepository;
    private final CourtRepository courtRepository;
    private final TimeSlotRepository timeSlotRepository;
    public CourtPricingServiceImpl(CourtPricingRepository courtPricingRepository,  CourtRepository courtRepository,  TimeSlotRepository timeSlotRepository) {
        this.courtPricingRepository = courtPricingRepository;
        this.courtRepository = courtRepository;
        this.timeSlotRepository = timeSlotRepository;
    }
    @Override
    public CreateCourtPricingResponse createCourtPricing(CreateCourtPricingRequest createCourtPricingRequest) {
        Court court = courtRepository.findById(createCourtPricingRequest.getCourtId())
                .orElseThrow(() -> new NotFoundException("Sân không tồn tại!"));

        TimeSlot timeSlot = timeSlotRepository.findById(createCourtPricingRequest.getTimeSlotId())
                .orElseThrow(() -> new NotFoundException("Khung giờ không tồn tại!"));
        Branch branch = court.getZone().getBranch();
        LocalTime openTime = branch.getOpenTime();
        LocalTime closeTime = branch.getCloseTime();
        LocalTime slotStart = timeSlot.getStartTime();
        LocalTime slotEnd = timeSlot.getEndTime();
        if (slotStart.isBefore(openTime) || slotEnd.isAfter(closeTime)) {
            throw new BadRequestException("Khung giờ không nằm trong giờ hoạt động của chi nhánh!");
        }
        boolean exists = courtPricingRepository
                .existsByCourtIdAndTimeSlotIdAndDayOfWeek(
                        createCourtPricingRequest.getCourtId(),
                        createCourtPricingRequest.getTimeSlotId(),
                        createCourtPricingRequest.getDayOfWeek()
                );
        if (exists) {
            throw new DuplicateResourceException(
                    "Sân này đã bị trùng timeSLot"
            );
        }
        CourtPricing pricing = new CourtPricing();
        pricing.setCourt(court);
        pricing.setDayOfWeek(createCourtPricingRequest.getDayOfWeek());
        pricing.setTimeSlot(timeSlot);
        pricing.setPrice(createCourtPricingRequest.getPrice());
        courtPricingRepository.save(pricing);
        return new CreateCourtPricingResponse("Tạo thành công!");
    }

    @Override
    public GetAllCourtPricingsResponse getAll(UUID branchId, UUID zoneId, UUID courtId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Pageable pageable) {
        Page<GetAllCourtPricingsResponse.Item> pageData =
                courtPricingRepository.search(
                        branchId, zoneId, courtId, dayOfWeek, startTime, endTime, pageable
                );

        return new GetAllCourtPricingsResponse(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages()
        );
    }
}
