package com.phucitdev.pickleball_backend.modules.auth.service;

import com.phucitdev.pickleball_backend.modules.auth.dto.*;


public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    CustomerRegisterResponse  customerRegister(CustomerRegisterRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    void verifyOtp(VerifyOtpRequest request);

}