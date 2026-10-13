package com.bandrehearsal.bandrehearsalmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "musicians")
public class Musician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    @NotBlank(message = "Instrument is required")
    @Size(max = 255)
    private String instrument;

    // A musician can play in many bands and a band has many musicians (ManyToMany, owning side).
    // A Set (not a List) makes the join table's primary key (musician_id, band_id), so the same
    // band cannot be linked twice.
    @ManyToMany
    @JoinTable(
            name = "band_musicians",
            joinColumns = @JoinColumn(name = "musician_id"),
            inverseJoinColumns = @JoinColumn(name = "band_id")
    )
    @OrderBy("name ASC")
    private Set<Band> bands = new LinkedHashSet<>();

    // One musician has many attendance records (OneToMany, inverse side)
    @JsonIgnore
    @OneToMany(mappedBy = "musician")
    private List<Attendance> attendances = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }

    public Set<Band> getBands() { return bands; }
    public void setBands(Set<Band> bands) { this.bands = bands; }

    public List<Attendance> getAttendances() { return attendances; }
    public void setAttendances(List<Attendance> attendances) { this.attendances = attendances; }
}
