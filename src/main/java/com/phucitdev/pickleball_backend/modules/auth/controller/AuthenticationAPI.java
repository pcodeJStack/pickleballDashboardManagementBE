package com.phucitdev.pickleball_backend.modules.auth.controller;
import com.phucitdev.pickleball_backend.commo.validation.sequence.LoginValidationOrder;
import com.phucitdev.pickleball_backend.commo.validation.sequence.RegisterValidationOrder;
import com.phucitdev.pickleball_backend.modules.auth.dto.LoginRequest;
import com.phucitdev.pickleball_backend.modules.auth.dto.LoginResponse;
import com.phucitdev.pickleball_backend.modules.auth.dto.RegisterRequest;
import com.phucitdev.pickleball_backend.modules.auth.dto.RegisterResponse;
import com.phucitdev.pickleball_backend.modules.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
}
