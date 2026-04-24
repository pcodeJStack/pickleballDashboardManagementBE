package com.phucitdev.pickleball_backend.commo.exception.timeslot;

public class InvalidTimeSlotException extends RuntimeException {
    public InvalidTimeSlotException(String message) {
        super(message);
    }
}