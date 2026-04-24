package com.phucitdev.pickleball_backend.modules.booking.entity;
import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import com.phucitdev.pickleball_backend.modules.auth.entity.CustomerProfile;
import com.phucitdev.pickleball_backend.modules.booking.dto.BookingStatus;
import com.phucitdev.pickleball_backend.modules.court.entity.Court;
import com.phucitdev.pickleball_backend.modules.court.entity.TimeSlot;
import com.phucitdev.pickleball_backend.modules.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "booking",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_booking_court_date_slot",
                        columnNames = {"court_id", "booking_date", "time_slot_id"}
                )
        }
)
public class Booking extends BaseEntity {
    private LocalDate bookingDate;  // ngày đặt
    private BigDecimal price;   // giá tại thời điểm đặt là bao nhiêu
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @ManyToOne(fetch = FetchType.LAZY) // sân nào
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;   // khung giờ nào

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerProfile customerProfile;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;
}
