package com.phucitdev.pickleball_backend.commo.exception.court;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
