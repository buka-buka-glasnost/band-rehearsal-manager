package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.models.Attendance;
import com.bandrehearsal.bandrehearsalmanager.repositories.IAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AttendanceService implements IAttendanceService{

    @Autowired
    private IAttendanceRepository attendanceRepository;

    @Override
    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance findById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    @Override
    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public void deleteById(Long id) {
        attendanceRepository.deleteById(id);
    }
}
