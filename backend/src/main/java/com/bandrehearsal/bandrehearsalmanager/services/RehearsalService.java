package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.exceptions.BadRequestException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ConflictException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ResourceNotFoundException;
import com.bandrehearsal.bandrehearsalmanager.models.Band;
import com.bandrehearsal.bandrehearsalmanager.models.Rehearsal;
import com.bandrehearsal.bandrehearsalmanager.models.RehearsalRoom;
import com.bandrehearsal.bandrehearsalmanager.repositories.IBandRepository;
import com.bandrehearsal.bandrehearsalmanager.repositories.IRehearsalRepository;
import com.bandrehearsal.bandrehearsalmanager.repositories.IRehearsalRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bandrehearsal.bandrehearsalmanager.services.ServiceSupport.referenceId;
import static com.bandrehearsal.bandrehearsalmanager.services.ServiceSupport.requireId;

@Service
public class RehearsalService implements IRehearsalService {

    private final IRehearsalRepository rehearsalRepository;
    private final IBandRepository bandRepository;
    private final IRehearsalRoomRepository rehearsalRoomRepository;

    public RehearsalService(IRehearsalRepository rehearsalRepository,
                            IBandRepository bandRepository,
                            IRehearsalRoomRepository rehearsalRoomRepository) {
        this.rehearsalRepository = rehearsalRepository;
        this.bandRepository = bandRepository;
        this.rehearsalRoomRepository = rehearsalRoomRepository;
    }

    @Override
    public List<Rehearsal> findAll() {
        return rehearsalRepository.findAll();
    }

    @Override
    public Rehearsal findById(Long id) {
        return rehearsalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rehearsal with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Rehearsal create(Rehearsal rehearsal) {
        rehearsal.setId(null);
        rehearsal.setBand(resolveBand(rehearsal.getBand()));
        rehearsal.setRehearsalRoom(resolveRoom(rehearsal.getRehearsalRoom()));
        return rehearsalRepository.save(rehearsal);
    }

    @Override
    @Transactional
    public Rehearsal update(Rehearsal rehearsal) {
        Rehearsal existing = findById(requireId(rehearsal.getId()));
        existing.setDate(rehearsal.getDate());
        existing.setDurationMinutes(rehearsal.getDurationMinutes());
        existing.setBand(resolveBand(rehearsal.getBand()));
        existing.setRehearsalRoom(resolveRoom(rehearsal.getRehearsalRoom()));
        return existing;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Rehearsal rehearsal = findById(id);
        if (!rehearsal.getAttendances().isEmpty()) {
            throw new ConflictException("Rehearsal on " + rehearsal.getDate() + " has attendance records; delete them first");
        }
        rehearsalRepository.delete(rehearsal);
    }

    // The client only sends { "id": ... } for related entities, so load the full entity
    private Band resolveBand(Band reference) {
        Long id = referenceId(reference.getId(), "band");
        return bandRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Band with id " + id + " does not exist"));
    }

    private RehearsalRoom resolveRoom(RehearsalRoom reference) {
        Long id = referenceId(reference.getId(), "rehearsalRoom");
        return rehearsalRoomRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Rehearsal room with id " + id + " does not exist"));
    }
}
