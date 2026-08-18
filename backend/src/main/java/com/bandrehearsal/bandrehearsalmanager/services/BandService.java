package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.Band;
import com.bandrehearsal.bandrehearsalmanager.repositories.IBandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BandService implements IBandService {

    @Autowired
    private IBandRepository bandRepository;

    @Override
    public List<Band> findAll() {
        return bandRepository.findAll();
    }

    @Override
    public Band findById(Long id) {
        return bandRepository.findById(id).orElse(null);
    }

    @Override
    public Band save(Band band) {
        return bandRepository.save(band);
    }

    @Override
    public void deleteById(Long id) {
        bandRepository.deleteById(id);
    }
}
