package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.RehearsalRoom;
import java.util.List;

public interface IRehearsalRoomService {
    List<RehearsalRoom> findAll();
    RehearsalRoom findById(Long id);
    RehearsalRoom save(RehearsalRoom rehearsalRoom);
    void deleteById(Long id);
}
