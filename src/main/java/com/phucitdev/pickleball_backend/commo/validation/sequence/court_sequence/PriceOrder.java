package com.phucitdev.pickleball_backend.commo.validation.sequence.court_sequence;

import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.MinPrice;
import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.PriceNotNull;
import jakarta.validation.GroupSequence;

@GroupSequence({PriceNotNull.class, MinPrice.class})
public interface PriceOrder {
}
