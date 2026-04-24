package com.phucitdev.pickleball_backend.commo.validation.sequence;

import com.phucitdev.pickleball_backend.commo.validation.group.register_group.EmailInValid;
import com.phucitdev.pickleball_backend.commo.validation.group.register_group.EmailNotBlank;
import jakarta.validation.GroupSequence;

@GroupSequence({EmailNotBlank.class, EmailInValid.class})
public interface EmailValidationOrder {
}
