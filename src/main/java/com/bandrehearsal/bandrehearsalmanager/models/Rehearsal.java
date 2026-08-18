package com.bandrehearsal.bandrehearsalmanager.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rehearsals")

public class Rehearsal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private Integer durationMinutes;

    @ManyToOne
    @JoinColumn(name = "band_id")
    private Band band;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RehearsalRoom rehearsalRoom;

    public RehearsalRoom getRehearsalRoom() {
        return rehearsalRoom;
    }

    public void setRehearsalRoom(RehearsalRoom rehearsalRoom) {
        this.rehearsalRoom = rehearsalRoom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }
}
