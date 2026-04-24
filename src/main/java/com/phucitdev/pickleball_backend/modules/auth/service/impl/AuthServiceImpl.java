package com.phucitdev.pickleball_backend.modules.auth.service.impl;
import com.phucitdev.pickleball_backend.commo.config.websocket.handler.AuthWebSocketHandler;
import com.phucitdev.pickleball_backend.commo.exception.auth.EmailAlreadyExistsException;
import com.phucitdev.pickleball_backend.commo.exception.auth.InvalidCredentialsException;
import com.phucitdev.pickleball_backend.commo.exception.auth.PhoneAlreadyExistsException;
import com.phucitdev.pickleball_backend.modules.auth.dto.*;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.auth.entity.Role;
import com.phucitdev.pickleball_backend.modules.auth.repository.AccountRepository;
import com.phucitdev.pickleball_backend.modules.auth.security.CustomUserDetails;
import com.phucitdev.pickleball_backend.modules.auth.security.JwtTokenProvider;
import com.phucitdev.pickleball_backend.modules.auth.service.AuthService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager  authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthWebSocketHandler authWebSocketHandler;
    public AuthServiceImpl(AccountRepository accountRepository,  PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                           RedisTemplate<String, Object> redisTemplate, AuthWebSocketHandler authWebSocketHandler) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
        this.authWebSocketHandler = authWebSocketHandler;
    }
    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        if (accountRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException();
        }
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setFullName(request.getFullName());
        account.setPhone(request.getPhone());
        account.setRole(Role.CUSTOMER);
        account.setIsActive(true);
        accountRepository.save(account);
        return new RegisterResponse("Account created successfully!");
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Account account = userDetails.getAccount();

            String accessToken = jwtTokenProvider.generateAccessToken(account.getId(), account.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(account, request.getDeviceId(), request.getDeviceInfo());

            String sessionId = UUID.randomUUID().toString();

            String refreshKey = "REFRESH_TOKEN:" + account.getId();
            redisTemplate.opsForValue().set(refreshKey, refreshToken, 7, TimeUnit.DAYS);
            authWebSocketHandler.forceLogout(account.getId(), sessionId);
            UserInfo user = new UserInfo();
            user.setId(account.getId().toString());
            user.setEmail(account.getEmail());
            user.setFullName(account.getFullName());
            user.setPhone(account.getPhone());
            user.setRole(account.getRole().name());

            return new LoginResponse(
                    accessToken,
                    refreshToken,
                    sessionId,
                    "Login successfully!",
                    user
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
    }

}
