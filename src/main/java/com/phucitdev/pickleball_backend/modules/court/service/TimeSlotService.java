package com.phucitdev.pickleball_backend.modules.court.service;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateNewTimeSlotRequest;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateNewTimeSlotResponse;
import com.phucitdev.pickleball_backend.modules.court.dto.GetAllTimeSlotsResponse;
import org.springframework.data.domain.Pageable;

public interface TimeSlotService {
    CreateNewTimeSlotResponse createNewTimeSlot(CreateNewTimeSlotRequest createNewTimeSlotRequest);
    GetAllTimeSlotsResponse getAllTimeSlots(Pageable pageable);
}
