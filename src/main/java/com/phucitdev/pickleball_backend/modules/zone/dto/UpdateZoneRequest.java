package com.phucitdev.pickleball_backend.modules.zone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateZoneRequest {
    @NotBlank(message = "Tên khu không được để trống")
    private String name;
    @NotBlank(message = "Mô tả không được để trống")
    private String description;
    @NotNull(message = "Số sân tối đa không được để trống")
    @Min(value = 1, message = "Số sân tối đa phải lớn hơn 0")
    private Integer maxCourts;
}
