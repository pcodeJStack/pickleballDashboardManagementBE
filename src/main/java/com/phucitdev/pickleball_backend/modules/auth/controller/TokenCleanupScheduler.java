package com.phucitdev.pickleball_backend.modules.auth.controller;
import com.phucitdev.pickleball_backend.modules.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class TokenCleanupScheduler {
    private final RefreshTokenRepository refreshTokenRepository;
    public TokenCleanupScheduler(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    @Scheduled(fixedRate = 60 * 1000) // mỗi 1 giờ
    @Transactional
    public void cleanExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredOrRevokedTokens();
    }
}
