package com.phucitdev.pickleball_backend.commo.validation.sequence.branch_sequence;
import com.phucitdev.pickleball_backend.commo.validation.group.createBranch_group.DescriptionNotBlank;
import com.phucitdev.pickleball_backend.commo.validation.group.createBranch_group.DescriptionSize;
import jakarta.validation.GroupSequence;
@GroupSequence({DescriptionNotBlank.class, DescriptionSize.class})
public interface DescriptionValidationOrder {
}
