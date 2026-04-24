package com.phucitdev.pickleball_backend.commo.validation.sequence.branch_sequence;

import com.phucitdev.pickleball_backend.commo.validation.group.createBranch_group.PhoneInvalid;
import com.phucitdev.pickleball_backend.commo.validation.group.createBranch_group.PhoneNotBlank;
import jakarta.validation.GroupSequence;

@GroupSequence({PhoneNotBlank.class, PhoneInvalid.class})
public interface PhoneValidationOrder {
}
