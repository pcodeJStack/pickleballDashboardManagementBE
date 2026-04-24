package com.phucitdev.pickleball_backend.commo.exception.auth;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException() {
        super("Phone already exists");
    }
}