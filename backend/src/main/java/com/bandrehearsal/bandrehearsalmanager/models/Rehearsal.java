package com.bandrehearsal.bandrehearsalmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rehearsals")
public class Rehearsal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 720, message = "Duration must be at most 720 minutes")
    private Integer durationMinutes;

    @NotNull(message = "Band is required")
    @ManyToOne
    @JoinColumn(name = "band_id")
    private Band band;

    @NotNull(message = "Rehearsal room is required")
    @ManyToOne
    @JoinColumn(name = "room_id")
    private RehearsalRoom rehearsalRoom;

    // One rehearsal has many attendance records (OneToMany, inverse side)
    @JsonIgnore
    @OneToMany(mappedBy = "rehearsal")
    private List<Attendance> attendances = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Band getBand() { return band; }
    public void setBand(Band band) { this.band = band; }

    public RehearsalRoom getRehearsalRoom() { return rehearsalRoom; }
    public void setRehearsalRoom(RehearsalRoom rehearsalRoom) { this.rehearsalRoom = rehearsalRoom; }

    public List<Attendance> getAttendances() { return attendances; }
    public void setAttendances(List<Attendance> attendances) { this.attendances = attendances; }
}
