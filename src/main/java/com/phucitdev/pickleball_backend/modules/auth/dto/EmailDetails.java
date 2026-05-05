package com.phucitdev.pickleball_backend.modules.auth.dto;

import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailDetails implements Serializable {
    private String email;
    private String subject;
    private String otpCode;
    private Date expiryTime;
}
