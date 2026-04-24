package com.phucitdev.pickleball_backend.commo.exception.branch;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException() {
        super("Chi nhánh không tồn tại!");
    }
}
