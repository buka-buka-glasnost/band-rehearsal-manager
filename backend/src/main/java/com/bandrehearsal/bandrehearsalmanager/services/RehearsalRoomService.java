package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.exceptions.ConflictException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ResourceNotFoundException;
import com.bandrehearsal.bandrehearsalmanager.models.RehearsalRoom;
import com.bandrehearsal.bandrehearsalmanager.repositories.IRehearsalRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bandrehearsal.bandrehearsalmanager.services.ServiceSupport.requireId;

@Service
public class RehearsalRoomService implements IRehearsalRoomService {

    private final IRehearsalRoomRepository rehearsalRoomRepository;

    public RehearsalRoomService(IRehearsalRoomRepository rehearsalRoomRepository) {
        this.rehearsalRoomRepository = rehearsalRoomRepository;
    }

    @Override
    public List<RehearsalRoom> findAll() {
        return rehearsalRoomRepository.findAll();
    }

    @Override
    public RehearsalRoom findById(Long id) {
        return rehearsalRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rehearsal room with id " + id + " not found"));
    }

    @Override
    @Transactional
    public RehearsalRoom create(RehearsalRoom room) {
        room.setId(null);
        return rehearsalRoomRepository.save(room);
    }

    @Override
    @Transactional
    public RehearsalRoom update(RehearsalRoom room) {
        RehearsalRoom existing = findById(requireId(room.getId()));
        existing.setName(room.getName());
        existing.setAddress(room.getAddress());
        existing.setCapacity(room.getCapacity());
        return existing;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        RehearsalRoom room = findById(id);
        if (!room.getRehearsals().isEmpty()) {
            throw new ConflictException("Rehearsal room '" + room.getName() + "' has rehearsals; delete them first");
        }
        rehearsalRoomRepository.delete(room);
    }
}
