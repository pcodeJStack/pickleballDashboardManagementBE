package com.phucitdev.pickleball_backend.commo.exception.auth;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Invalid token");
    }
}