package com.phucitdev.pickleball_backend.commo.validation.sequence.branch_sequence;

import com.phucitdev.pickleball_backend.commo.validation.group.createBranch_group.AddressNotBlank;
import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.CloseTimeNotNull;
import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.NameNotBlank;
import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.OpenTimeNotNull;
import jakarta.validation.GroupSequence;

@GroupSequence({NameNotBlank.class, AddressNotBlank.class, PhoneValidationOrder.class, ImageUrlValidationOrder.class, DescriptionValidationOrder.class, OpenTimeNotNull.class, CloseTimeNotNull.class})
public interface UpdateBranchValidationOrder {
}
