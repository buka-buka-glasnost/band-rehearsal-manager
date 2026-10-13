package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.exceptions.ConflictException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ResourceNotFoundException;
import com.bandrehearsal.bandrehearsalmanager.models.Band;
import com.bandrehearsal.bandrehearsalmanager.models.Musician;
import com.bandrehearsal.bandrehearsalmanager.repositories.IBandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bandrehearsal.bandrehearsalmanager.services.ServiceSupport.requireId;

@Service
public class BandService implements IBandService {

    private final IBandRepository bandRepository;

    public BandService(IBandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    @Override
    public List<Band> findAll() {
        return bandRepository.findAll();
    }

    @Override
    public Band findById(Long id) {
        return bandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Band with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Band create(Band band) {
        band.setId(null);
        return bandRepository.save(band);
    }

    @Override
    @Transactional
    public Band update(Band band) {
        Band existing = findById(requireId(band.getId()));
        existing.setName(band.getName());
        existing.setGenre(band.getGenre());
        existing.setFoundedYear(band.getFoundedYear());
        return existing;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Band band = findById(id);
        if (!band.getRehearsals().isEmpty()) {
            throw new ConflictException("Band '" + band.getName() + "' has rehearsals; delete them first");
        }
        // Musician is the owning side of the ManyToMany, so the join rows are removed through it
        for (Musician musician : band.getMusicians()) {
            musician.getBands().remove(band);
        }
        bandRepository.delete(band);
    }
}
