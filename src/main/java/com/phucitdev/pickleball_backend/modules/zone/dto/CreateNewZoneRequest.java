package com.phucitdev.pickleball_backend.modules.zone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewZoneRequest {
    @NotBlank(message = "Tên khu không được để trống")
    private String name;
    @NotBlank(message = "Mô tả không được để trống")
    private String description;
    @NotNull(message = "Số sân tối đa không được để trống")
    @Min(value = 1, message = "Số sân tối đa phải lớn hơn 0")
    private Integer maxCourts;
    @NotNull(message = "BranchId là bắt buộc")
    private UUID branchId;
}
