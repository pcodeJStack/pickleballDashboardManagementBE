package com.phucitdev.pickleball_backend.modules.court.service.impl;
import com.phucitdev.pickleball_backend.commo.exception.timeslot.InvalidTimeSlotException;
import com.phucitdev.pickleball_backend.modules.court.dto.*;
import com.phucitdev.pickleball_backend.modules.court.entity.TimeSlot;
import com.phucitdev.pickleball_backend.modules.court.repository.TimeSlotRepository;
import com.phucitdev.pickleball_backend.modules.court.service.TimeSlotService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }
    @Override
    public CreateNewTimeSlotResponse createNewTimeSlot(CreateNewTimeSlotRequest createNewTimeSlotRequest) {
        if (!createNewTimeSlotRequest.getEndTime().isAfter(createNewTimeSlotRequest.getStartTime())) {
            throw new InvalidTimeSlotException("Thời gian kết thúc phải sau thời gian bắt đầu!");
        }
        boolean isOverlap = timeSlotRepository.existsOverlapping(
                createNewTimeSlotRequest.getStartTime(),
                createNewTimeSlotRequest.getEndTime()
        );
        if (isOverlap) {
            throw new InvalidTimeSlotException("Time slot đã bị trùng");
        }
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(createNewTimeSlotRequest.getStartTime());
        timeSlot.setEndTime(createNewTimeSlotRequest.getEndTime());
        timeSlotRepository.save(timeSlot);
        return new CreateNewTimeSlotResponse("Tạo time slot thành công!");
    }

    @Override
    public GetAllTimeSlotsResponse getAllTimeSlots(Pageable pageable) {
        Page<TimeSlot> pageData = timeSlotRepository.findByIsDeletedFalse(pageable);
        List<TimeSlotItem> items = pageData.getContent()
                .stream()
                .map(ts -> {
                    TimeSlotItem item = new TimeSlotItem();
                    item.setId(ts.getId());
                    item.setStartTime(ts.getStartTime().toString());
                    item.setEndTime(ts.getEndTime().toString());
                    item.setCreatedAt(ts.getCreatedAt());
                    item.setUpdatedAt(ts.getUpdatedAt());
                    return item;
                })
                .toList();
        GetAllTimeSlotsResponse response = new GetAllTimeSlotsResponse();
        response.setItems(items);
        response.setPage(pageData.getNumber());
        response.setSize(pageData.getSize());
        response.setTotalElements(pageData.getTotalElements());
        response.setTotalPages(pageData.getTotalPages());
        return response;
    }
}
