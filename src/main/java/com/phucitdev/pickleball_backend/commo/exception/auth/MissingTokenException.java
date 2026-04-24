package com.phucitdev.pickleball_backend.commo.exception.auth;

public class MissingTokenException extends RuntimeException {
    public MissingTokenException() {
        super("Token is missing");
    }
}