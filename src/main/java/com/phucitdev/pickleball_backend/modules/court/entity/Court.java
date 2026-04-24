package com.phucitdev.pickleball_backend.modules.court.entity;
import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import com.phucitdev.pickleball_backend.modules.branch.entity.Branch;
import com.phucitdev.pickleball_backend.modules.zone.entity.Zone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"zone_id", "court_number"})
        }
)
public class Court extends BaseEntity {

   private String name;
   private String description;
   private Integer courtNumber;
   @Enumerated(EnumType.STRING)
   private CourtType courtType;

   @Enumerated(EnumType.STRING)
   private CourtSurfaceType surfaceType;

   @Enumerated(EnumType.STRING)
   private CourtStatus courtStatus;

   private String imageUrl;
   private String location;
   private Integer maxPlayers;

   @OneToMany(mappedBy = "court")
   private List<CourtImage> courtImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourtPricing> pricings;

    @OneToMany(mappedBy = "court")
    private List<Booking> bookings;

}
