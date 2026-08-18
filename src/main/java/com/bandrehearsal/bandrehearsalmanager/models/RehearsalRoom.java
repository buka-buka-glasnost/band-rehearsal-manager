package com.bandrehearsal.bandrehearsalmanager.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "rehearsal_rooms")
public class RehearsalRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private Integer capacity;

    @OneToMany(mappedBy = "rehearsalRoom")
    private List<Rehearsal> rehearsals;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Rehearsal> getRehearsals() {
        return rehearsals;
    }

    public void setRehearsals(List<Rehearsal> rehearsals) {
        this.rehearsals = rehearsals;
    }
}
