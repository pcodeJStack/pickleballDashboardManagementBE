package com.phucitdev.pickleball_backend.commo.validation.sequence;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.FullNameNotBlank;
import jakarta.validation.GroupSequence;
@GroupSequence({EmailValidationOrder.class, PasswordValidationOrder.class, FullNameNotBlank.class, PhoneValidationOrder.class })
public interface RegisterValidationOrder {
}
