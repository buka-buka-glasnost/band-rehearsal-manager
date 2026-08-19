package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.Rehearsal;
import com.bandrehearsal.bandrehearsalmanager.repositories.IRehearsalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RehearsalService implements IRehearsalService {

    @Autowired
    private IRehearsalRepository rehearsalRepository;

    @Override
    public List<Rehearsal> findAll() {
        return rehearsalRepository.findAll();
    }

    @Override
    public Rehearsal findById(Long id) {
        return rehearsalRepository.findById(id).orElse(null);
    }

    @Override
    public Rehearsal save(Rehearsal rehearsal) {
        return rehearsalRepository.save(rehearsal);
    }

    @Override
    public void deleteById(Long id) {
        rehearsalRepository.deleteById(id);
    }
}
