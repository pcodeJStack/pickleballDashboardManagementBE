package com.phucitdev.pickleball_backend.modules.auth.dto;

import com.phucitdev.pickleball_backend.commo.validation.group.login_group.DeviceIdNotBlank;
import com.phucitdev.pickleball_backend.commo.validation.group.login_group.DeviceInfoNotBlank;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.EmailInValid;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.EmailNotBlank;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.PasswordInValid;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.PasswordNotBlank;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email must not be blank!", groups = {EmailNotBlank.class})
    @Email(message = "Email is invalid!", groups = {EmailInValid.class})
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password must not be blank!", groups = {PasswordNotBlank.class})
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "Password must be at least 6 characters, include uppercase, number and special character", groups = {PasswordInValid.class}
    )
    private String password;

    @NotBlank(message = "DeviceId must not be blank!", groups = {DeviceIdNotBlank.class})
    private String deviceId;
    @NotBlank(message = "DeviceInfo must not be blank!", groups = {DeviceInfoNotBlank.class})
    private String deviceInfo;
}
