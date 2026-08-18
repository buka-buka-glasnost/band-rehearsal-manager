package com.bandrehearsal.bandrehearsalmanager.repositories;

import com.bandrehearsal.bandrehearsalmanager.models.Band;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBandRepository extends JpaRepository<Band, Long> {
}
