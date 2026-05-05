package com.phucitdev.pickleball_backend.modules.auth.controller;
import com.phucitdev.pickleball_backend.commo.validation.sequence.LoginValidationOrder;
import com.phucitdev.pickleball_backend.commo.validation.sequence.RegisterValidationOrder;
import com.phucitdev.pickleball_backend.modules.auth.dto.*;
import com.phucitdev.pickleball_backend.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class AuthenticationAPI {
    private final AuthService authService;
    @Autowired
    public AuthenticationAPI(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/api/auth/register")
    public ResponseEntity<RegisterResponse>  register(@Validated(RegisterValidationOrder.class) @RequestBody RegisterRequest registerRequest) {
            RegisterResponse registerResponse = authService.register(registerRequest);
            return ResponseEntity.ok(registerResponse);
    }
    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponse>  login(@Validated(LoginValidationOrder.class) @RequestBody LoginRequest loginRequest) {
         LoginResponse loginResponse = authService.login(loginRequest);
         return ResponseEntity.ok(loginResponse);
    }
    @PostMapping("/api/auth/customer/register")
    public ResponseEntity<CustomerRegisterResponse> registerCustomer(@Validated(RegisterValidationOrder.class) @RequestBody  CustomerRegisterRequest customerRegisterRequest) {
        CustomerRegisterResponse customerRegisterResponse = authService.customerRegister(customerRegisterRequest);
        return ResponseEntity.ok(customerRegisterResponse);
    }
    @PostMapping("/api/auth/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(refreshTokenResponse);
    }
    @PostMapping("/api/auth/verify-otp")
    public ResponseEntity verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest){
        authService.verifyOtp(verifyOtpRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message","Verify OTP Successful");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/api/auth/resend-otp")
    public ResponseEntity resendOtp(@Valid @RequestBody ResendOtpRequest resendOtpRequest){
        authService.resendOtp(resendOtpRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message","Resend OTP Successful");
        return ResponseEntity.ok(response);
    }
}
