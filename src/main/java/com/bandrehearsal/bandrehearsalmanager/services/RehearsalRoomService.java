package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.RehearsalRoom;
import com.bandrehearsal.bandrehearsalmanager.repositories.IRehearsalRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RehearsalRoomService implements IRehearsalRoomService {

    @Autowired
    private IRehearsalRoomRepository rehearsalRoomRepository;

    @Override
    public List<RehearsalRoom> findAll() {
        return rehearsalRoomRepository.findAll();
    }

    @Override
    public RehearsalRoom findById(Long id) {
        return rehearsalRoomRepository.findById(id).orElse(null);

    }

    @Override
    public RehearsalRoom save(RehearsalRoom rehearsalRoom) {
        return rehearsalRoomRepository.save(rehearsalRoom);
    }
    @Override
    public void deleteById(Long id) {
        rehearsalRoomRepository.deleteById(id);
    }
}
