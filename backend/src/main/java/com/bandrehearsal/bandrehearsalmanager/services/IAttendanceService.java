package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.Attendance;
import java.util.List;

public interface IAttendanceService {
    List<Attendance> findAll();
    Attendance findById(Long id);
    Attendance save(Attendance attendance);
    void deleteById(Long id);
}
