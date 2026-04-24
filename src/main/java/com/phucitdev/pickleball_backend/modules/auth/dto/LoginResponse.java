package com.phucitdev.pickleball_backend.modules.auth.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String sessionId;
    private String message;
    private UserInfo userInfo;
}
