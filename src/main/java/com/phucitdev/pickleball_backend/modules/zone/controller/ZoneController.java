package com.phucitdev.pickleball_backend.modules.zone.controller;

import com.phucitdev.pickleball_backend.commo.validation.sequence.court_sequence.CreateCourtValidationOrder;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateNewCourtRequest;
import com.phucitdev.pickleball_backend.modules.court.dto.CreateNewCourtResponse;
import com.phucitdev.pickleball_backend.modules.court.dto.GetAllCourtsResponse;
import com.phucitdev.pickleball_backend.modules.zone.dto.*;
import com.phucitdev.pickleball_backend.modules.zone.entity.Zone;
import com.phucitdev.pickleball_backend.modules.zone.repository.ZoneRepository;
import com.phucitdev.pickleball_backend.modules.zone.service.ZoneService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ZoneController {
    private final ZoneService zoneService;
    public ZoneController( ZoneService zoneService) {
        this.zoneService = zoneService;
    }
    @PostMapping("/api/zone")
    public ResponseEntity<CreateNewZoneResponse> createNewCourt(@Valid  @RequestBody CreateNewZoneRequest createNewZoneRequest) {
        CreateNewZoneResponse createNewZoneResponse = zoneService.createNewZone(createNewZoneRequest);
        return ResponseEntity.ok().body(createNewZoneResponse);
    }
    @GetMapping("/api/zones")
    public ResponseEntity<GetAllZonesResponse> getAllZones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) String name
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(zoneService.getAllZones(
                branchId,
                name,
                pageable));
    }
    @PutMapping("/api/zone/{id}")
    public ResponseEntity<UpdateZoneResponse> updateZone(@PathVariable UUID id, @Valid  @RequestBody UpdateZoneRequest updateZoneRequest) {
        UpdateZoneResponse updateZoneResponse = zoneService.updateZone(id, updateZoneRequest);
        return ResponseEntity.ok().body(updateZoneResponse);
    }
    @DeleteMapping("/api/zone/{id}")
    public ResponseEntity<DeleteZoneResponse> deleteZone(@PathVariable UUID id) {
        DeleteZoneResponse deleteZoneResponse = zoneService.deleteZone(id);
        return ResponseEntity.ok().body(deleteZoneResponse);
    }
}
