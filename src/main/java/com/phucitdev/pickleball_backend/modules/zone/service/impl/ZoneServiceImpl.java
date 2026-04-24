package com.phucitdev.pickleball_backend.modules.zone.service.impl;

import com.phucitdev.pickleball_backend.commo.exception.branch.BranchNotFoundException;
import com.phucitdev.pickleball_backend.commo.exception.court.DuplicateResourceException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import com.phucitdev.pickleball_backend.modules.branch.repository.BranchRepository;
import com.phucitdev.pickleball_backend.modules.zone.dto.*;
import com.phucitdev.pickleball_backend.modules.zone.entity.Zone;
import com.phucitdev.pickleball_backend.modules.zone.repository.ZoneRepository;
import com.phucitdev.pickleball_backend.modules.zone.service.ZoneService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ZoneServiceImpl implements ZoneService {
    private final ZoneRepository zoneRepository;
    private final BranchRepository branchRepository;
    public ZoneServiceImpl(ZoneRepository zoneRepository, BranchRepository branchRepository) {
        this.zoneRepository = zoneRepository;
        this.branchRepository = branchRepository;
    }
    @Override
    public CreateNewZoneResponse createNewZone(CreateNewZoneRequest createNewZoneRequest) {
        Branch branch = branchRepository.findById(createNewZoneRequest.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException());

        boolean exists = zoneRepository.existsByBranchIdAndName(
                createNewZoneRequest.getBranchId(),
                createNewZoneRequest.getName()
        );
        if (exists) {
            throw new DuplicateResourceException("Khu đã tồn tại trong chi nhánh");
        }
        Zone zone = new Zone();
        zone.setName(createNewZoneRequest.getName());
        zone.setDescription(createNewZoneRequest.getDescription());
        zone.setMaxCourts(createNewZoneRequest.getMaxCourts());
        zone.setBranch(branch);
        zoneRepository.save(zone);
        return new CreateNewZoneResponse("Tạo khu thành công !");
    }

    @Override
    public GetAllZonesResponse getAllZones(UUID branchId, String name, Pageable pageable) {
        name = normalize(name);
        Page<ZoneResponse> pageData;
        if (branchId != null) {
            pageData = zoneRepository.searchByBranch(branchId, name, pageable);
        } else {
            pageData = zoneRepository.searchAll(name, pageable);
        }
        return new GetAllZonesResponse(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages()
        );
    }
    @Override
    public UpdateZoneResponse updateZone(UUID id, UpdateZoneRequest updateZoneRequest) {
        Zone zone = zoneRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Zone not found!"));
        zone.setName(updateZoneRequest.getName());
        zone.setDescription(updateZoneRequest.getDescription());
        zone.setMaxCourts(updateZoneRequest.getMaxCourts());
        zoneRepository.save(zone);
        return new UpdateZoneResponse("Cập nhật thành công!");
    }

    @Override
    public DeleteZoneResponse deleteZone(UUID id) {
        Zone zone = zoneRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Zone not found!"));
        zone.setIsDeleted(true);
        zoneRepository.save(zone);
        return new DeleteZoneResponse("Delete zone successfully");
    }
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

}
