package com.phucitdev.pickleball_backend.commo.exception.notfound;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
