package com.phucitdev.pickleball_backend.modules.court.service.impl;
import com.phucitdev.pickleball_backend.commo.exception.court.BadRequestException;
import com.phucitdev.pickleball_backend.commo.exception.court.DuplicateResourceException;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.commo.exception.zone.ZoneNotFoundException;
import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import com.phucitdev.pickleball_backend.modules.court.dto.*;
import com.phucitdev.pickleball_backend.modules.court.entity.Court;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtStatus;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtSurfaceType;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtType;
import com.phucitdev.pickleball_backend.modules.court.repository.CourtRepository;
import com.phucitdev.pickleball_backend.modules.court.service.CourtService;
import com.phucitdev.pickleball_backend.modules.zone.entity.Zone;
import com.phucitdev.pickleball_backend.modules.zone.repository.ZoneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CourtServiceImpl implements CourtService {
    private final CourtRepository courtRepository;
    private final ZoneRepository zoneRepository;
    public CourtServiceImpl(CourtRepository courtRepository, ZoneRepository zoneRepository) {
        this.courtRepository = courtRepository;
        this.zoneRepository = zoneRepository;
    }
    @Override
    public CreateNewCourtResponse createNewCourt(CreateNewCourtRequest createNewCourtRequest) {
        Zone zone = zoneRepository.findById(createNewCourtRequest.getZoneId())
                .orElseThrow(() -> new ZoneNotFoundException("Khu không tồn tại!"));
        if (zone.getCourts().size() >= zone.getMaxCourts()) {
            throw new BadRequestException("Khu đã đạt số lượng sân tối đa!");
        }
        boolean exists = courtRepository.existsByZoneIdAndCourtNumber(
                zone.getId(), createNewCourtRequest.getCourtNumber());
        if (exists) {
            throw new DuplicateResourceException("Thứ tự sân đã bị trùng trong khu!");
        }
        Court court = new Court();
        court.setName(createNewCourtRequest.getName());
        court.setCourtNumber(createNewCourtRequest.getCourtNumber());
        court.setDescription(createNewCourtRequest.getDescription());
        court.setCourtType(createNewCourtRequest.getCourtType());
        court.setSurfaceType(createNewCourtRequest.getSurfaceType());
        court.setCourtStatus(createNewCourtRequest.getCourtStatus());
        court.setImageUrl(createNewCourtRequest.getImageUrl());
        court.setLocation(createNewCourtRequest.getLocation());
        court.setMaxPlayers(createNewCourtRequest.getMaxPlayers());
        court.setZone(zone);
        courtRepository.save(court);
        return new CreateNewCourtResponse("Sận được tạo thành công!");
    }

    @Override
    public GetAllCourtsResponse getAllCourts(UUID branchId, UUID zoneId, String name, CourtType courtType, CourtSurfaceType surfaceType, CourtStatus courtStatus, Pageable pageable) {
                name = normalize(name);
        Page<CourtResponse> pageData = courtRepository.search(name, courtType, surfaceType,courtStatus, branchId, zoneId,
                pageable
        );
                return new GetAllCourtsResponse(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages()
        );
    }

    @Override
    public UpdateCourtResponse updateCourt(UUID id, UpdateCourtRequest updateCourtRequest) {
        Court court = courtRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Court not found!"));

        Zone newZone = zoneRepository.findByIdAndIsDeletedFalse(updateCourtRequest.getZoneId())
                .orElseThrow(() -> new NotFoundException("Zone not found!"));

        Zone currentZone = court.getZone();
        if (!court.getCourtNumber().equals(updateCourtRequest.getCourtNumber())
                || !currentZone.getId().equals(newZone.getId())) {
            boolean exists = courtRepository.existsByZoneIdAndCourtNumber(
                    newZone.getId(),
                    updateCourtRequest.getCourtNumber()
            );
            if (exists) {
                throw new DuplicateResourceException("Thứ tự sân đã bị trùng trong khu!");
            }
        }
        court.setName(updateCourtRequest.getName());
        court.setCourtNumber(updateCourtRequest.getCourtNumber());
        court.setDescription(updateCourtRequest.getDescription());
        court.setCourtType(updateCourtRequest.getCourtType());
        court.setSurfaceType(updateCourtRequest.getSurfaceType());
        court.setCourtStatus(updateCourtRequest.getCourtStatus());
        court.setImageUrl(updateCourtRequest.getImageUrl());
        court.setLocation(updateCourtRequest.getLocation());
        court.setMaxPlayers(updateCourtRequest.getMaxPlayers());

        court.setZone(newZone);
        courtRepository.save(court);
        return new UpdateCourtResponse("Cập nhật sân thành công!");
    }

    @Override
    public DeleteCourtResponse deleteCourt(UUID courtId) {
        Court court = courtRepository.findByIdAndIsDeletedFalse(courtId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sân!"));
        court.setIsDeleted(true);
        courtRepository.save(court);
        return new DeleteCourtResponse("Xoá sân thành công!");
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

}
