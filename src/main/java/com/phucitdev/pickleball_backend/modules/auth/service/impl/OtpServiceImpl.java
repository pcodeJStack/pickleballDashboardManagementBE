package com.phucitdev.pickleball_backend.modules.auth.service.impl;

import com.phucitdev.pickleball_backend.commo.exception.notfound.NotFoundException;
import com.phucitdev.pickleball_backend.commo.exception.otp.InvalidOtpException;
import com.phucitdev.pickleball_backend.commo.exception.otp.OtpAlreadyUsedException;
import com.phucitdev.pickleball_backend.commo.exception.otp.OtpExpiredException;
import com.phucitdev.pickleball_backend.commo.exception.otp.OtpNotFoundException;
import com.phucitdev.pickleball_backend.modules.auth.entity.Account;
import com.phucitdev.pickleball_backend.modules.auth.entity.OtpVerification;
import com.phucitdev.pickleball_backend.modules.auth.repository.AccountRepository;
import com.phucitdev.pickleball_backend.modules.auth.repository.OtpRepository;
import com.phucitdev.pickleball_backend.modules.auth.service.OtpService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class OtpServiceImpl  implements OtpService {
    private final OtpRepository otpRepository;
    private final AccountRepository accountRepository;
    public  OtpServiceImpl(OtpRepository otpRepository, AccountRepository accountRepository) {
        this.otpRepository = otpRepository;
        this.accountRepository = accountRepository;
    }
    private final Random random = new Random();
    @Override
    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // 6 chữ số
        return String.valueOf(otp);
    }

    @Override
    public boolean validateOtp(Account account, String otpInput) {

        OtpVerification otp = otpRepository
                .findTopByAccountOrderByCreatedAtDesc(account)
                .orElseThrow(() -> new OtpNotFoundException("Không tìm thấy OTP, vui lòng yêu cầu lại"));

        // check OTP đúng
        if (!otp.getOtpCode().equals(otpInput)) {
            throw new InvalidOtpException("OTP không chính xác");
        }

        // check đã dùng
        if (Boolean.TRUE.equals(otp.getUsed())) {
            throw new OtpAlreadyUsedException("OTP đã được sử dụng");
        }
        // check hết hạn
        if (otp.getExpiryTime().before(new Date())) {
            throw new OtpExpiredException("OTP đã hết hạn");
        }

        // update trạng thái
        otp.setUsed(true);
        account.setIsActive(true);
        otpRepository.save(otp);
        accountRepository.save(account);
        return true;
    }
}
