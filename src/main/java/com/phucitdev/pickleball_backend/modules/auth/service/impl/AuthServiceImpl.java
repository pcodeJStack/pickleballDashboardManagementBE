package com.phucitdev.pickleball_backend.modules.auth.service.impl;
import com.phucitdev.pickleball_backend.commo.config.websocket.handler.AuthWebSocketHandler;
import com.phucitdev.pickleball_backend.commo.exception.auth.*;
import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.modules.auth.dto.*;
import com.phucitdev.pickleball_backend.modules.auth.entity.*;
import com.phucitdev.pickleball_backend.modules.auth.repository.AccountRepository;
import com.phucitdev.pickleball_backend.modules.auth.repository.OtpRepository;
import com.phucitdev.pickleball_backend.modules.auth.repository.RefreshTokenRepository;
import com.phucitdev.pickleball_backend.modules.auth.security.CustomUserDetails;
import com.phucitdev.pickleball_backend.modules.auth.security.JwtTokenProvider;
import com.phucitdev.pickleball_backend.modules.auth.service.AuthService;
import com.phucitdev.pickleball_backend.modules.auth.service.EmailService;
import com.phucitdev.pickleball_backend.modules.auth.service.OtpService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    public AuthServiceImpl(AccountRepository accountRepository,  PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                           RedisTemplate<String, Object> redisTemplate, AuthWebSocketHandler authWebSocketHandler,
                           RefreshTokenRepository refreshTokenRepository,
                           OtpRepository otpRepository,
                           OtpService otpService,
                           EmailService emailService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
        this.authWebSocketHandler = authWebSocketHandler;
        this.refreshTokenRepository = refreshTokenRepository;
        this.otpService = otpService;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
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
        account.setRole(Role.ADMIN);
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

    @Override
    public CustomerRegisterResponse customerRegister(CustomerRegisterRequest request) {
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
        CustomerProfile cp = new CustomerProfile();
        cp.setAccount(account);
        cp.setAvatar(null);
        account.setCustomerProfile(cp);
        Account acc =  accountRepository.save(account);
        String otp  = otpService.generateOtp();
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(acc.getEmail());
        otpVerification.setOtpCode(otp);
        otpVerification.setExpiryTime(new Date(System.currentTimeMillis() + 1 * 60 * 1000));
        otpVerification.setAccount(acc);
        otpRepository.save(otpVerification);
        // gửi mail
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setReceiver(acc);
        emailDetails.setSubject("Your OTP Code for Account Verification");
        emailDetails.setOtpCode(otp);
        emailDetails.setExpiryTime( new Date(System.currentTimeMillis() + 60_000));
        emailService.sendOtpMail(emailDetails);
        return  new CustomerRegisterResponse("Tài khoảng đăng ký thành công");
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken  = request.getRefreshToken();
        jwtTokenProvider.validateRefreshToken(refreshToken);
        RefreshToken tokenInDb = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(InvalidTokenException::new);
        if (tokenInDb.isRevoked()) {
            throw new InvalidTokenException();
        }
        if (tokenInDb.getExpiredAt().before(new Date())) {
            tokenInDb.setRevoked(true);
            refreshTokenRepository.save(tokenInDb);
            throw new TokenExpiredException();
        }
        Account account = tokenInDb.getAccount();
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                account.getId(),
                account.getEmail()
        );
        return new  RefreshTokenResponse(newAccessToken);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new NotFoundException("Account not found with email: " + request.getEmail())
                );
        otpService.validateOtp(account, request.getOtpInput());
    }

}
