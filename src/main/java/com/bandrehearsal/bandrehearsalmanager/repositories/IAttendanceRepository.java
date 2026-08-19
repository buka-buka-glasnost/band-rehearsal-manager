package com.bandrehearsal.bandrehearsalmanager.repositories;

import com.bandrehearsal.bandrehearsalmanager.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAttendanceRepository extends JpaRepository<Attendance, Long> {
}
