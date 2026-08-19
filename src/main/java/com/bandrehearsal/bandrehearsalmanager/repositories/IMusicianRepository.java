package com.bandrehearsal.bandrehearsalmanager.repositories;

import com.bandrehearsal.bandrehearsalmanager.models.Musician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMusicianRepository extends JpaRepository<Musician, Long> {
}
