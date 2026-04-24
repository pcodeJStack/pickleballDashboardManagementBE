package com.phucitdev.pickleball_backend.commo.exception.auth;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super("Account not found");
    }
}
