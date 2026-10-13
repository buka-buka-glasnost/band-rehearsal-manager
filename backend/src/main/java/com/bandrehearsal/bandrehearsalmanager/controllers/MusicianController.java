package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.Musician;
import com.bandrehearsal.bandrehearsalmanager.services.IMusicianService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/musician")
public class MusicianController {

    private final IMusicianService musicianService;

    public MusicianController(IMusicianService musicianService) {
        this.musicianService = musicianService;
    }

    @GetMapping("/get-list")
    public List<Musician> getList() {
        return musicianService.findAll();
    }

    @GetMapping("/get/{id}")
    public Musician getById(@PathVariable Long id) {
        return musicianService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Musician> create(@Valid @RequestBody Musician musician) {
        return ResponseEntity.status(HttpStatus.CREATED).body(musicianService.create(musician));
    }

    @PutMapping("/update")
    public Musician update(@Valid @RequestBody Musician musician) {
        return musicianService.update(musician);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        musicianService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
