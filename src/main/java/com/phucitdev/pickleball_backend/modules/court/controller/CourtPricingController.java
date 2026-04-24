package com.phucitdev.pickleball_backend.modules.court.controller;

import com.phucitdev.pickleball_backend.modules.court.dto.CreateCourtPricingRequest;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateCourtPricingResponse;
import com.phucitdev.pickleball_backend.modules.court.dto.GetAllCourtPricingsResponse;
import com.phucitdev.pickleball_backend.modules.court.service.CourtPricingService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CourtPricingController {
    private final CourtPricingService courtPricingService;
    public CourtPricingController(CourtPricingService courtPricingService) {
        this.courtPricingService = courtPricingService;
    }
    @PostMapping("/api/court-pricings")
    public ResponseEntity<CreateCourtPricingResponse> createCourtPricing(@Valid @RequestBody CreateCourtPricingRequest createCourtPricingRequest){
        CreateCourtPricingResponse createCourtPricingResponse =  courtPricingService.createCourtPricing(createCourtPricingRequest);
        return ResponseEntity.ok().body(createCourtPricingResponse);
    }
    @GetMapping("/api/court-pricings")
    public ResponseEntity<GetAllCourtPricingsResponse> getAllCourtPricing(
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) UUID zoneId,
            @RequestParam(required = false) UUID courtId,
            @RequestParam(required = false) DayOfWeek dayOfWeek,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @Schema(
                    type = "string",
                    example = "07:00:00",
                    pattern = "^([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$"
            ) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) @Schema(
                    type = "string",
                    example = "07:00:00",
                    pattern = "^([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$"
            ) LocalTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        GetAllCourtPricingsResponse response = courtPricingService.getAll(
                branchId, zoneId, courtId, dayOfWeek, startTime, endTime, pageable
        );
        return ResponseEntity.ok(response);
    }
}
