package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.Band;

import java.util.List;

public interface IBandService {
    List<Band> findAll();
    Band findById(Long id);
    Band create(Band band);
    Band update(Band band);
    void deleteById(Long id);
}
