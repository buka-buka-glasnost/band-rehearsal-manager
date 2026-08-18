package com.bandrehearsal.bandrehearsalmanager.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "bands")
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String genre;
    private Integer foundedYear;

    @OneToMany(mappedBy = "band")
    private List<Rehearsal> rehearsals;

    @ManyToMany(mappedBy = "bands")
    private List<Musician> musicians;

    public List<Musician> getMusicians() {
        return musicians;
    }

    public void setMusicians(List<Musician> musicians) {
        this.musicians = musicians;
    }

    public List<Rehearsal> getRehearsals() {
        return rehearsals;
    }

    public void setRehearsals(List<Rehearsal> rehearsals) {
        this.rehearsals = rehearsals;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id;}

    public String getName() {return name;}
    public void setName(String name) { this.name = name; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) {this.genre = genre;}

    public Integer getFoundedYear() { return foundedYear; }
    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }

}
