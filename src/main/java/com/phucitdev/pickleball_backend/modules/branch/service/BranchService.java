package com.phucitdev.pickleball_backend.modules.branch.service;

import com.phucitdev.pickleball_backend.modules.branch.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BranchService{
    CreateNewBranchResponse createNewBranch(CreateNewBranchRequest createNewBranchRequest);
    GetAllBranchesResponse getBranches(Pageable pageable, String name, String address, String phone);
    DeleteBranchResponse deleteBranch(UUID branchId);
    UpdateBranchResponse updateBranch(UUID branchId, UpdateBranchRequest updateBranchRequest);
}
