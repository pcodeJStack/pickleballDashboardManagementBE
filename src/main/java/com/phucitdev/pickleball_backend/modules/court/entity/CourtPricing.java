package com.phucitdev.pickleball_backend.modules.court.entity;
import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.DayOfWeek;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CourtPricing extends BaseEntity {
    @Column(nullable = false)
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;
    @ManyToOne(fetch = FetchType.LAZY)
    private TimeSlot timeSlot;
}
