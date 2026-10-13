package com.bandrehearsal.bandrehearsalmanager.repositories;

import com.bandrehearsal.bandrehearsalmanager.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByMusician_IdAndRehearsal_Id(Long musicianId, Long rehearsalId);

    boolean existsByMusician_IdAndRehearsal_IdAndIdNot(Long musicianId, Long rehearsalId, Long id);
}
