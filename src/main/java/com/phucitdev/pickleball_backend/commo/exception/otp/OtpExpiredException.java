package com.phucitdev.pickleball_backend.commo.exception.otp;

public class OtpExpiredException extends RuntimeException {
    public OtpExpiredException(String message) {
        super(message);
    }
}
