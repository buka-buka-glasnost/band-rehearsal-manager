package com.bandrehearsal.bandrehearsalmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "attendances")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Musician is required")
    @ManyToOne
    @JoinColumn(name = "musician_id")
    private Musician musician;

    @NotNull(message = "Rehearsal is required")
    @ManyToOne
    @JoinColumn(name = "rehearsal_id")
    private Rehearsal rehearsal;

    @NotNull(message = "Present flag is required")
    private Boolean present;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Musician getMusician() { return musician; }
    public void setMusician(Musician musician) { this.musician = musician; }

    public Rehearsal getRehearsal() { return rehearsal; }
    public void setRehearsal(Rehearsal rehearsal) { this.rehearsal = rehearsal; }

    public Boolean getPresent() { return present; }
    public void setPresent(Boolean present) { this.present = present; }
}
