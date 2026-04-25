package com.phucitdev.pickleball_backend.modules.branch.repository;

import com.phucitdev.pickleball_backend.modules.branch.dto.BranchResponse;
import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
    boolean existsByNameAndOwnerId(String name, UUID ownerId);
    @Query("""
SELECT new com.phucitdev.pickleball_backend.modules.branch.dto.BranchResponse(
    b.id,
    b.name,
    b.address,
    b.phone,
    b.imageUrl,
    b.description,
     b.openTime,
    b.closeTime,
    b.createdAt,
    b.updatedAt
)
FROM Branch b
WHERE b.isDeleted = false
AND (:name IS NULL OR b.name LIKE CONCAT(:name, '%'))
AND (:address IS NULL OR b.address LIKE CONCAT(:address, '%'))
AND (:phone IS NULL OR b.phone LIKE CONCAT(:phone, '%'))
""")
    Page<BranchResponse> searchBranches(

            String name,
            String address,
            String phone,
            Pageable pageable
    );
    Optional<Branch> findByIdAndIsDeletedFalse(UUID branchId);
    Optional<Branch> findById(UUID branchId);

}
