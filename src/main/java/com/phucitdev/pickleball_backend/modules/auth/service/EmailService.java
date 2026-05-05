package com.phucitdev.pickleball_backend.modules.auth.service;

import com.phucitdev.pickleball_backend.modules.auth.dto.EmailDetails;

public interface EmailService {
    void sendOtpMail(EmailDetails emailDetails);
}
