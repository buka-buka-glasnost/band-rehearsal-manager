package com.bandrehearsal.bandrehearsalmanager.repositories;

import com.bandrehearsal.bandrehearsalmanager.models.RehearsalRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRehearsalRoomRepository extends JpaRepository<RehearsalRoom, Long> {
}
