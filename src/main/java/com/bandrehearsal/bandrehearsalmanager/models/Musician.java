package com.bandrehearsal.bandrehearsalmanager.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "musicians")

public class Musician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String instrument;

    @ManyToMany
    @JoinTable(
            name = "band_musicians",
            joinColumns = @JoinColumn(name = "musician_id"),
            inverseJoinColumns =  @JoinColumn(name = "band_id")
    )
    private List<Band> bands;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public List<Band> getBands() {
        return bands;
    }

    public void setBands(List<Band> bands) {
        this.bands = bands;
    }
}
