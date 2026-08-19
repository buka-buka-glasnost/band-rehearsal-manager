package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.Musician;
import com.bandrehearsal.bandrehearsalmanager.repositories.IMusicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MusicianService implements IMusicianService {

    @Autowired
    private IMusicianRepository musicianRepository;

    @Override
    public List<Musician> findAll(){
        return musicianRepository.findAll();
    }

    @Override
    public Musician findById(Long id) {
        return musicianRepository.findById(id).orElse(null);
    }

    @Override
    public Musician save(Musician musician) {
        return musicianRepository.save(musician);
    }

    @Override
    public void deleteById(Long id) {
        musicianRepository.deleteById(id);
    }
}
