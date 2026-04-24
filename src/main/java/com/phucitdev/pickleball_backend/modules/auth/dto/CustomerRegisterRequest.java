package com.phucitdev.pickleball_backend.modules.auth.dto;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.*;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterRequest {
    @NotBlank(message = "Email must not be blank!", groups = {EmailNotBlank.class})
    @Email(message = "Email is invalid!", groups = {EmailInValid.class})
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password must not be blank!", groups = {PasswordNotBlank.class})
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "Password must be at least 6 characters, include uppercase, number and special character", groups = {PasswordInValid.class}
    )
    private String password;

    @NotBlank(message = "FullName must not be blank!", groups = {FullNameNotBlank.class})
    private String fullName;

    @NotBlank(message = "Phone must not be blank!", groups = {PhoneNotBlank.class})
    @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$",
            message = "Phone number is invalid!", groups = {PhoneInValid.class}
    )
    @Column(unique = true)
    private String phone;
}
