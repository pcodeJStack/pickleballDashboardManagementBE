package com.phucitdev.pickleball_backend.modules.auth.entity;

import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OtpVerification extends BaseEntity {
    String email;
    String otpCode;
    Date expiryTime;
    @Schema(defaultValue = "false")
    Boolean used = false;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account;
}
