package com.phucitdev.pickleball_backend.commo.validation.sequence.court_sequence;

import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.MaxPlayersNotNull;
import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.MinMaxPlayers;
import jakarta.validation.GroupSequence;

@GroupSequence({MaxPlayersNotNull.class, MinMaxPlayers.class})
public interface MaxPlayersOrder {
}
