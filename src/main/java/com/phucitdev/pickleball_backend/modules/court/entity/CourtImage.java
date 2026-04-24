package com.phucitdev.pickleball_backend.modules.court.entity;

import com.phucitdev.pickleball_backend.commo.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "court_images")
public class CourtImage extends BaseEntity {
    private String imageUrl;
    private String altText; // mô tả ảnh
    private Integer sortOrder; // thứ tự hiển thị
    private boolean isPrimary = false; // ảnh đại diện

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;
}