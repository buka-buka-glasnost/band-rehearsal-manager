package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.RehearsalRoom;

import java.util.List;

public interface IRehearsalRoomService {
    List<RehearsalRoom> findAll();
    RehearsalRoom findById(Long id);
    RehearsalRoom create(RehearsalRoom room);
    RehearsalRoom update(RehearsalRoom room);
    void deleteById(Long id);
}
