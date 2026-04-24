package com.phucitdev.pickleball_backend.commo.exception.auth;

public class InvalidTokenTypeException extends RuntimeException {
    public InvalidTokenTypeException() {
        super("Invalid token type");
    }
}