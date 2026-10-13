package com.bandrehearsal.bandrehearsalmanager.services;

import com.bandrehearsal.bandrehearsalmanager.exceptions.BadRequestException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ConflictException;
import com.bandrehearsal.bandrehearsalmanager.exceptions.ResourceNotFoundException;
import com.bandrehearsal.bandrehearsalmanager.models.Attendance;
import com.bandrehearsal.bandrehearsalmanager.models.Musician;
import com.bandrehearsal.bandrehearsalmanager.models.Rehearsal;
import com.bandrehearsal.bandrehearsalmanager.repositories.IAttendanceRepository;
import com.bandrehearsal.bandrehearsalmanager.repositories.IMusicianRepository;
import com.bandrehearsal.bandrehearsalmanager.repositories.IRehearsalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bandrehearsal.bandrehearsalmanager.services.ServiceSupport.referenceId;
import static com.bandrehearsal.bandrehearsalmanager.services.ServiceSupport.requireId;

@Service
public class AttendanceService implements IAttendanceService {

    private final IAttendanceRepository attendanceRepository;
    private final IMusicianRepository musicianRepository;
    private final IRehearsalRepository rehearsalRepository;

    public AttendanceService(IAttendanceRepository attendanceRepository,
                             IMusicianRepository musicianRepository,
                             IRehearsalRepository rehearsalRepository) {
        this.attendanceRepository = attendanceRepository;
        this.musicianRepository = musicianRepository;
        this.rehearsalRepository = rehearsalRepository;
    }

    @Override
    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance findById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Attendance create(Attendance attendance) {
        Musician musician = resolveMusician(attendance.getMusician());
        Rehearsal rehearsal = resolveRehearsal(attendance.getRehearsal());
        if (attendanceRepository.existsByMusician_IdAndRehearsal_Id(musician.getId(), rehearsal.getId())) {
            throw duplicate(musician);
        }
        attendance.setId(null);
        attendance.setMusician(musician);
        attendance.setRehearsal(rehearsal);
        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public Attendance update(Attendance attendance) {
        Attendance existing = findById(requireId(attendance.getId()));
        Musician musician = resolveMusician(attendance.getMusician());
        Rehearsal rehearsal = resolveRehearsal(attendance.getRehearsal());
        if (attendanceRepository.existsByMusician_IdAndRehearsal_IdAndIdNot(
                musician.getId(), rehearsal.getId(), existing.getId())) {
            throw duplicate(musician);
        }
        existing.setMusician(musician);
        existing.setRehearsal(rehearsal);
        existing.setPresent(attendance.getPresent());
        return existing;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        attendanceRepository.delete(findById(id));
    }

    private Musician resolveMusician(Musician reference) {
        Long id = referenceId(reference.getId(), "musician");
        return musicianRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Musician with id " + id + " does not exist"));
    }

    private Rehearsal resolveRehearsal(Rehearsal reference) {
        Long id = referenceId(reference.getId(), "rehearsal");
        return rehearsalRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Rehearsal with id " + id + " does not exist"));
    }

    private static ConflictException duplicate(Musician musician) {
        return new ConflictException("Attendance for '" + musician.getName() + "' at this rehearsal already exists");
    }
}
