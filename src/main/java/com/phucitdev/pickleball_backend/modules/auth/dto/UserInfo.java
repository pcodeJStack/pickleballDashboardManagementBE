package com.phucitdev.pickleball_backend.modules.auth.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserInfo {
    private String id;
    private String email;
    private String fullName;
    private String phone;
    private String role;
}