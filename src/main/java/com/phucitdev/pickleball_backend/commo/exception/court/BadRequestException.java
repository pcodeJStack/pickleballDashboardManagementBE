package com.phucitdev.pickleball_backend.commo.exception.court;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

}