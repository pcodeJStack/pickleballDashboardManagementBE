package com.phucitdev.pickleball_backend.modules.branch.entity;

import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.court.entity.Court;
import com.phucitdev.pickleball_backend.modules.zone.entity.Zone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(
        name = "branches",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_branch_name_owner", columnNames = {"name", "owner_id"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Branch extends BaseEntity {
    private String name;
    private String address;
    private String phone;
    private String imageUrl;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Account owner;
    @OneToMany(mappedBy = "branch")
    private List<Zone> zones;
}