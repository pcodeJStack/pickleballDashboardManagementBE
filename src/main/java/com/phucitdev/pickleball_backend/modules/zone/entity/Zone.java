package com.phucitdev.pickleball_backend.modules.zone.entity;

import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import com.phucitdev.pickleball_backend.modules.court.entity.Court;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "zone",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"branch_id", "name"})
        }
)
public class Zone extends BaseEntity {
    private String name;
    private String description;
    private Integer maxCourts;
    @OneToMany(mappedBy = "zone")
    private List<Court> courts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
}
