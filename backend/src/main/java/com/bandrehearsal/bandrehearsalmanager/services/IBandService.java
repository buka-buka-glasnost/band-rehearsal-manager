package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.Band;
import java.util.List;

public interface IBandService {
    List<Band> findAll();
    Band findById(Long id);
    Band save(Band band);
    void deleteById(Long id);
}
