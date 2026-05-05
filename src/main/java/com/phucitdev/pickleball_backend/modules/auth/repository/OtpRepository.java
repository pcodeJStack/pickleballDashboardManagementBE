package com.phucitdev.pickleball_backend.modules.auth.repository;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.auth.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
public interface OtpRepository extends JpaRepository<OtpVerification, UUID> {
   Optional<OtpVerification> findTopByAccountOrderByCreatedAtDesc(Account account);
}
