package com.phucitdev.pickleball_backend.modules.branch.dto;
import com.phucitdev.pickleball_backend.commo.validation.group.createBranch_group.*;
import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.NameNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBranchRequest {
    @NotBlank(message = "Branch name must not be blank!",  groups = {NameNotBlank.class})
    private String name;

    @NotBlank(message = "Address must not be blank!", groups = {AddressNotBlank.class})
    private String address;

    @NotBlank(message = "Phone must not be blank!", groups = {PhoneNotBlank.class})
    @Pattern(regexp = "^(0|\\+84)[0-9]{9}$", message = "Phone number is invalid", groups = {PhoneInvalid.class})
    private String phone;

    @NotBlank(message = "ImageUrl must not be blank!", groups = {ImageUrlNotBlank.class})
    @Size(max = 255, message = "Image URL is too long", groups = {ImageUrlSize.class})
    private String imageUrl;

    @NotBlank(message = "Description must not be blank!", groups = {DescriptionNotBlank.class})
    @Size(max = 500, message = "Description too long", groups = {DescriptionSize.class})
    private String description;

    @NotNull(message = "OpenTime must not be null!", groups = {OpenTimeNotNull.class})
    private LocalTime openTime;
    @NotNull(message = "CloseTime must not be null",  groups = {CloseTimeNotNull.class})
    private LocalTime closeTime;
}
