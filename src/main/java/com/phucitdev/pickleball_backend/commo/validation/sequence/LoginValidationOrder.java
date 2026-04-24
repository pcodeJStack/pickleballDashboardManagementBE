package com.phucitdev.pickleball_backend.commo.validation.sequence;

import com.phucitdev.pickleball_backend.commo.validation.group.login_group.DeviceIdNotBlank;
import com.phucitdev.pickleball_backend.commo.validation.group.login_group.DeviceInfoNotBlank;
import jakarta.validation.GroupSequence;

@GroupSequence({EmailValidationOrder.class, PasswordValidationOrder.class, DeviceIdNotBlank.class, DeviceInfoNotBlank.class})
public interface LoginValidationOrder {
}
