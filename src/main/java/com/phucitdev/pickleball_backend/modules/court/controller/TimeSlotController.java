package com.phucitdev.pickleball_backend.modules.court.controller;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateNewTimeSlotRequest;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateNewTimeSlotResponse;
import com.phucitdev.pickleball_backend.modules.court.dto.GetAllTimeSlotsResponse;
import com.phucitdev.pickleball_backend.modules.court.entity.TimeSlot;
import com.phucitdev.pickleball_backend.modules.court.service.TimeSlotService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    public TimeSlotController(TimeSlotService timeSlotService){
        this.timeSlotService = timeSlotService;
    }
    @PostMapping("/api/timeslot")
    public ResponseEntity<CreateNewTimeSlotResponse> createNewTimeSlot(@Valid @RequestBody CreateNewTimeSlotRequest  createNewTimeSlotRequest) {
        CreateNewTimeSlotResponse createNewTimeSlotResponse = timeSlotService.createNewTimeSlot(createNewTimeSlotRequest);
        return ResponseEntity.ok().body(createNewTimeSlotResponse);
    }
    @GetMapping("/api/timeSlots")
    public ResponseEntity<GetAllTimeSlotsResponse> getAllTimeSlots(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(timeSlotService.getAllTimeSlots(pageable));
    }
}
