package com.phucitdev.pickleball_backend.commo.validation.sequence;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.PhoneInValid;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.PhoneNotBlank;
import jakarta.validation.GroupSequence;

@GroupSequence({PhoneNotBlank.class, PhoneInValid.class})
public interface PhoneValidationOrder {
}
