package com.phucitdev.pickleball_backend.commo.validation.sequence;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.PasswordInValid;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.PasswordNotBlank;
import jakarta.validation.GroupSequence;

@GroupSequence({PasswordNotBlank.class, PasswordInValid.class})
public interface PasswordValidationOrder {
}
