package com.phucitdev.pickleball_backend.commo.exception.auth;

public class InvalidDeviceIdException extends RuntimeException {
    public InvalidDeviceIdException() {
        super("Invalid DeviceId");
    }
}
