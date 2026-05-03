package com.phucitdev.pickleball_backend.modules.auth.repository;
import com.phucitdev.pickleball_backend.modules.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
     Optional<RefreshToken> findByToken(String token);
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiredAt < CURRENT_TIMESTAMP OR rt.revoked = true")
    void deleteAllExpiredOrRevokedTokens();

}
