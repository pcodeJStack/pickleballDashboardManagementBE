package com.phucitdev.pickleball_backend.commo.exception.otp;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String message) {
        super(message);
    }
}
