package com.phucitdev.pickleball_backend.modules.branch.controller;
import com.phucitdev.pickleball_backend.commo.validation.sequence.branch_sequence.CreateNewBranchValidationOrder;
import com.phucitdev.pickleball_backend.commo.validation.sequence.branch_sequence.UpdateBranchValidationOrder;
import com.phucitdev.pickleball_backend.modules.branch.dto.*;
import com.phucitdev.pickleball_backend.modules.branch.service.BranchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class BranchController {
    private final BranchService branchService;
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }
    @PostMapping("/api/branch")
    public ResponseEntity<CreateNewBranchResponse> createNewBranch(@Validated(CreateNewBranchValidationOrder.class) @RequestBody CreateNewBranchRequest createNewBranchRequest) {
          CreateNewBranchResponse createNewBranchResponse = branchService.createNewBranch(createNewBranchRequest);
          return ResponseEntity.ok().body(createNewBranchResponse);
    }
    @GetMapping("/api/branches")
    public ResponseEntity<GetAllBranchesResponse> getBranches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(branchService.getBranches(pageable, name, address, phone));
    }
    @DeleteMapping("/api/branch/{id}")
    public ResponseEntity<DeleteBranchResponse> deleteBranch(@PathVariable UUID id){
        DeleteBranchResponse deleteBranchResponse = branchService.deleteBranch(id);
        return ResponseEntity.ok().body(deleteBranchResponse);
    }
    @PutMapping("/api/branch/{id}")
    public ResponseEntity<UpdateBranchResponse> updateBranch(@PathVariable UUID id, @Validated(UpdateBranchValidationOrder.class) @RequestBody UpdateBranchRequest updateBranchRequest) {
          UpdateBranchResponse updateBranchResponse = branchService.updateBranch(id, updateBranchRequest);
          return ResponseEntity.ok().body(updateBranchResponse);
    }
}
