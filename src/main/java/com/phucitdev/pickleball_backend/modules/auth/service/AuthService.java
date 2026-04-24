package com.phucitdev.pickleball_backend.modules.auth.service;

import com.phucitdev.pickleball_backend.modules.auth.dto.LoginRequest;
import com.phucitdev.pickleball_backend.modules.auth.dto.LoginResponse;
import com.phucitdev.pickleball_backend.modules.auth.dto.RegisterRequest;
import com.phucitdev.pickleball_backend.modules.auth.dto.RegisterResponse;


public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}