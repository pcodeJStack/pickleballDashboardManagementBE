package com.phucitdev.pickleball_backend.modules.auth.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "accounts")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Account extends BaseEntity {
    @Column(unique = true)
    private String email;
    private String password;
    private String fullName;
    @Column(unique = true)
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isActive = true;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "owner")
    private List<Branch>  branches;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private CustomerProfile customerProfile;
    @OneToMany(mappedBy = "account")
    private List<OtpVerification>  otpVerifications;
}
