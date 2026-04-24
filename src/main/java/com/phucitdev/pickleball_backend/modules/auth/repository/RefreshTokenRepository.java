package com.phucitdev.pickleball_backend.modules.auth.repository;
import com.phucitdev.pickleball_backend.modules.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

}
