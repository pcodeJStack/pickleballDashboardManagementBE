package com.phucitdev.pickleball_backend.modules.auth.repository;

import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean  existsByEmail(String email);
    boolean  existsByPhone(String phone);
    Optional<Account> findByEmail(String email);
}
