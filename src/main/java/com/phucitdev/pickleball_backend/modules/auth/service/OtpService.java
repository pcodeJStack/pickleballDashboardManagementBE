package com.phucitdev.pickleball_backend.modules.auth.service;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
public interface OtpService {
    String generateOtp();
    boolean validateOtp(Account account,  String otpInput);
    boolean resendOtp(Account account);
}
