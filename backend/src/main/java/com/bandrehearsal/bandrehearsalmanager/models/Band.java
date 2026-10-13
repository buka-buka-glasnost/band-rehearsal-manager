package com.bandrehearsal.bandrehearsalmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bands")
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    @NotBlank(message = "Genre is required")
    @Size(max = 255)
    private String genre;

    @Min(value = 1900, message = "Founded year must be 1900 or later")
    @Max(value = 2100, message = "Founded year must be 2100 or earlier")
    private Integer foundedYear;

    // One band has many rehearsals (OneToMany, inverse side)
    @JsonIgnore
    @OneToMany(mappedBy = "band")
    private List<Rehearsal> rehearsals = new ArrayList<>();

    // Ignored in JSON to avoid an infinite loop, since Musician already serializes its bands
    @JsonIgnore
    @ManyToMany(mappedBy = "bands")
    private List<Musician> musicians = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getFoundedYear() { return foundedYear; }
    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }

    public List<Rehearsal> getRehearsals() { return rehearsals; }
    public void setRehearsals(List<Rehearsal> rehearsals) { this.rehearsals = rehearsals; }

    public List<Musician> getMusicians() { return musicians; }
    public void setMusicians(List<Musician> musicians) { this.musicians = musicians; }
}
