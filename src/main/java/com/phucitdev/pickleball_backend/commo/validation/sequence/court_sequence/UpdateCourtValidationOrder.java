package com.phucitdev.pickleball_backend.commo.validation.sequence.court_sequence;
import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.*;
import jakarta.validation.GroupSequence;

@GroupSequence({NameNotBlank.class,CourtNumberNotNull.class, DescriptionNotBlank.class, ImageNotBlank.class, CourtSurfaceTypeNotNull.class,  CourtTypeNotNull.class, CourtStatusNotNull.class, MaxPlayersOrder.class, LocationNotBlank.class})
public interface UpdateCourtValidationOrder {
}
