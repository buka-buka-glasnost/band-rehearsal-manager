package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.Musician;
import java.util.List;

public interface IMusicianService {
    List<Musician> findAll();
    Musician findById(Long id);
    Musician save(Musician musician);
    void deleteById(Long id);
}
