package com.bandrehearsal.bandrehearsalmanager.repositories;

import com.bandrehearsal.bandrehearsalmanager.models.Rehearsal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRehearsalRepository extends JpaRepository<Rehearsal, Long> {
}
