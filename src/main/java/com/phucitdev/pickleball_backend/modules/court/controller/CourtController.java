package com.phucitdev.pickleball_backend.modules.court.controller;
import com.phucitdev.pickleball_backend.commo.validation.sequence.branch_sequence.UpdateBranchValidationOrder;
import com.phucitdev.pickleball_backend.commo.validation.sequence.court_sequence.CreateCourtValidationOrder;
import com.phucitdev.pickleball_backend.commo.validation.sequence.court_sequence.UpdateCourtValidationOrder;
import com.phucitdev.pickleball_backend.modules.branch.dto.UpdateBranchRequest;
import com.phucitdev.pickleball_backend.modules.court.dto.*;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtStatus;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtSurfaceType;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtType;
import com.phucitdev.pickleball_backend.modules.court.service.CourtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;
@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CourtController {
    private final CourtService courtService;
    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }
    @PostMapping("/api/court")
    public ResponseEntity<CreateNewCourtResponse> createNewCourt(@Validated(CreateCourtValidationOrder.class) @RequestBody CreateNewCourtRequest createNewCourtRequest){
           CreateNewCourtResponse createNewCourtResponse = courtService.createNewCourt(createNewCourtRequest);
           return ResponseEntity.ok().body(createNewCourtResponse);
    }
    @GetMapping("/api/courts")
    public ResponseEntity<GetAllCourtsResponse> getAllCourts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) UUID zoneId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) CourtType courtType,
            @RequestParam(required = false) CourtSurfaceType surfaceType,
            @RequestParam(required = false) CourtStatus courtStatus
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(courtService.getAllCourts(
                branchId,
                zoneId,
                name,
                courtType,
                surfaceType,
                courtStatus,
                pageable));

    }
    @PutMapping("/api/court/{id}")
    public ResponseEntity<UpdateCourtResponse>  updateCourt(@PathVariable UUID id, @Validated(UpdateCourtValidationOrder.class) @RequestBody UpdateCourtRequest updateCourtRequest){
             UpdateCourtResponse updateCourtResponse = courtService.updateCourt(id, updateCourtRequest);
             return ResponseEntity.ok().body(updateCourtResponse);
    }
    @DeleteMapping("/api/court/{id}")
    public ResponseEntity<DeleteCourtResponse>  deleteCourt(@PathVariable UUID id){
          DeleteCourtResponse deleteCourtResponse = courtService.deleteCourt(id);
          return ResponseEntity.ok().body(deleteCourtResponse);
    }
}
