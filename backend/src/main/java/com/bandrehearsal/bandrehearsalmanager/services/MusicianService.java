package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.exceptions.BadRequestException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ConflictException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ResourceNotFoundException;
import com.bandrehearsal.bandrehearsalmanager.models.Band;
import com.bandrehearsal.bandrehearsalmanager.models.Musician;
import com.bandrehearsal.bandrehearsalmanager.repositories.IBandRepository;
import com.bandrehearsal.bandrehearsalmanager.repositories.IMusicianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bandrehearsal.bandrehearsalmanager.services.ServiceSupport.referenceId;
import static com.bandrehearsal.bandrehearsalmanager.services.ServiceSupport.requireId;

@Service
public class MusicianService implements IMusicianService {

    private final IMusicianRepository musicianRepository;
    private final IBandRepository bandRepository;

    public MusicianService(IMusicianRepository musicianRepository, IBandRepository bandRepository) {
        this.musicianRepository = musicianRepository;
        this.bandRepository = bandRepository;
    }

    @Override
    public List<Musician> findAll() {
        return musicianRepository.findAll();
    }

    @Override
    public Musician findById(Long id) {
        return musicianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Musician with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Musician create(Musician musician) {
        Set<Band> bands = resolveBands(musician.getBands());
        musician.setId(null);
        musician.setBands(bands);
        return musicianRepository.save(musician);
    }

    @Override
    @Transactional
    public Musician update(Musician musician) {
        Musician existing = findById(requireId(musician.getId()));
        Set<Band> bands = resolveBands(musician.getBands());
        existing.setName(musician.getName());
        existing.setInstrument(musician.getInstrument());
        existing.getBands().clear();
        existing.getBands().addAll(bands);
        return existing;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Musician musician = findById(id);
        if (!musician.getAttendances().isEmpty()) {
            throw new ConflictException("Musician '" + musician.getName() + "' has attendance records; delete them first");
        }
        musicianRepository.delete(musician);
    }

    /** The client sends bands as [{ "id": 1 }, ...]; load the real entities and reject unknown ids. */
    private Set<Band> resolveBands(Set<Band> references) {
        if (references == null || references.isEmpty()) {
            return new LinkedHashSet<>();
        }
        Set<Long> ids = references.stream()
                .map(band -> referenceId(band.getId(), "bands"))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Band> bands = bandRepository.findAllById(ids);
        if (bands.size() != ids.size()) {
            Set<Long> found = bands.stream().map(Band::getId).collect(Collectors.toSet());
            ids.removeAll(found);
            throw new BadRequestException("Unknown band id(s): " + ids);
        }
        // Same order as when the musician is loaded later (@OrderBy("name ASC"))
        bands.sort(Comparator.comparing(Band::getName, String.CASE_INSENSITIVE_ORDER));
        return new LinkedHashSet<>(bands);
    }
}
