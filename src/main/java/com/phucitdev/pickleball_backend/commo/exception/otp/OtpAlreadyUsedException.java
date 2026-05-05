package com.phucitdev.pickleball_backend.commo.exception.otp;
public class OtpAlreadyUsedException extends RuntimeException {
    public OtpAlreadyUsedException(String message) {
        super(message);
    }
}