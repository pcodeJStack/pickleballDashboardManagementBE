package com.phucitdev.pickleball_backend.modules.auth.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class VerifyOtpRequest {
    @NotBlank(message = "Email must not be blank!")
    private String email;
    @NotBlank(message = "OtpInput must not be blank!")
    private String otpInput;
}
