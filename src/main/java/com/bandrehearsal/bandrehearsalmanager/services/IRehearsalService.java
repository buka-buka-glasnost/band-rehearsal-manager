package com.bandrehearsal.bandrehearsalmanager.services;

import  com.bandrehearsal.bandrehearsalmanager.models.Rehearsal;
import java.util.List;

public interface IRehearsalService {
    List<Rehearsal> findAll();
    Rehearsal findById(Long id);
    Rehearsal save(Rehearsal rehearsal);
    void deleteById(Long id);
}
