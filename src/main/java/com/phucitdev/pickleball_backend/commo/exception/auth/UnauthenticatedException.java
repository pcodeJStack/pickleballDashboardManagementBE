package com.phucitdev.pickleball_backend.commo.exception.auth;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super("Unauthenticated");
    }
}