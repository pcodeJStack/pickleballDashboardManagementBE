package com.phucitdev.pickleball_backend.modules.branch.service.impl;
import com.phucitdev.pickleball_backend.commo.exception.branch.BranchNotFoundException;
import com.phucitdev.pickleball_backend.commo.exception.court.DuplicateResourceException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.commo.exception.timeslot.InvalidTimeSlotException;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.auth.security.SecurityUtils;
import com.phucitdev.pickleball_backend.modules.branch.dto.*;
import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import com.phucitdev.pickleball_backend.modules.branch.repository.BranchRepository;
import com.phucitdev.pickleball_backend.modules.branch.service.BranchService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }
    @Override
    public CreateNewBranchResponse createNewBranch(CreateNewBranchRequest createNewBranchRequest) {
        Account acc = SecurityUtils.getCurrentAccount();
        if (branchRepository.existsByNameAndOwnerId(createNewBranchRequest.getName(), acc.getId())) {
            throw new DuplicateResourceException("Tên chi nhánh đã tồn tại!");
        }
        if (!createNewBranchRequest.getCloseTime().isAfter(createNewBranchRequest.getOpenTime())) {
            throw new InvalidTimeSlotException("Thời gian đóng cửa phải sau thời gian mở!");
        }
        Branch branch = new Branch();
        branch.setName(createNewBranchRequest.getName());
        branch.setAddress(createNewBranchRequest.getAddress());
        branch.setPhone(createNewBranchRequest.getPhone());
        branch.setImageUrl(createNewBranchRequest.getImageUrl());
        branch.setDescription(createNewBranchRequest.getDescription());
        branch.setOpenTime(createNewBranchRequest.getOpenTime());
        branch.setCloseTime(createNewBranchRequest.getCloseTime());
        branch.setOwner(acc);
        branchRepository.save(branch);
        return new CreateNewBranchResponse("Create new branch successfully");
    }
    @Override
    public GetAllBranchesResponse getMyBranches(Pageable pageable, String name, String address, String phone) {
        Account acc = SecurityUtils.getCurrentAccount();
        name = normalize(name);
        address = normalize(address);
        phone = normalize(phone);
        Page<BranchResponse> pageData = branchRepository.searchBranches(acc.getId(), name, address, phone, pageable);
        return new GetAllBranchesResponse(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages()
        );
    }

    @Override
    public DeleteBranchResponse deleteBranch(UUID branchId) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(branchId)
                .orElseThrow(() -> new NotFoundException("Branch not found!"));
        branch.setIsDeleted(true);
        branchRepository.save(branch);
        return new DeleteBranchResponse("Xoá chi nhánh thành công!");
    }

    @Override
    public UpdateBranchResponse updateBranch(UUID branchId, UpdateBranchRequest updateBranchRequest) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(branchId)
                .orElseThrow(() -> new NotFoundException("Branch not found!"));
        if (!updateBranchRequest.getCloseTime().isAfter(updateBranchRequest.getOpenTime())) {
            throw new InvalidTimeSlotException("Thời gian đóng cửa phải sau thời gian mở!");
        }
        branch.setName(updateBranchRequest.getName());
        branch.setAddress(updateBranchRequest.getAddress());
        branch.setOpenTime(updateBranchRequest.getOpenTime());
        branch.setCloseTime(updateBranchRequest.getCloseTime());
        branch.setPhone(updateBranchRequest.getPhone());
        branch.setImageUrl(updateBranchRequest.getImageUrl());
        branch.setDescription(updateBranchRequest.getDescription());
        try {
            branchRepository.save(branch);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("uk_branch_name_owner")) {
                throw new DuplicateResourceException("Tên chi nhánh đã bị trùng");
            }
            throw ex;
        }
        return new UpdateBranchResponse("Cập nhật chi nhánh thành công!");
    }
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
