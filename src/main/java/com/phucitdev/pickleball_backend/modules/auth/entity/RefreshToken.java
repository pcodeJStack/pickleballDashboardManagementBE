package com.phucitdev.pickleball_backend.modules.auth.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(nullable = false)
    private UUID deviceId;
    @Column(nullable = false)
    private Date createdAt;
    @Column(nullable = false)
    private Date expiredAt;
    @Column(nullable = false)
    private boolean revoked = false;
    private String deviceInfo;
    private String ipAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}

