package com.phucitdev.pickleball_backend.modules.auth.entity;

import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import com.phucitdev.pickleball_backend.modules.booking.entity.Booking;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CustomerProfile extends BaseEntity {
    private String avatar;
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @OneToMany(mappedBy = "customerProfile")
    private List<Booking> bookings;
}
