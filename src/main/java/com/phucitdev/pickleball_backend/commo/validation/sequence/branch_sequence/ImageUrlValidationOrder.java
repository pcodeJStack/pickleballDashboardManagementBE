package com.phucitdev.pickleball_backend.commo.validation.sequence.branch_sequence;

import com.phucitdev.pickleball_backend.commo.validation.group.createBranch_group.ImageUrlNotBlank;
import com.phucitdev.pickleball_backend.commo.validation.group.createBranch_group.ImageUrlSize;
import jakarta.validation.GroupSequence;

@GroupSequence({ImageUrlNotBlank.class, ImageUrlSize.class})
public interface ImageUrlValidationOrder {
}
