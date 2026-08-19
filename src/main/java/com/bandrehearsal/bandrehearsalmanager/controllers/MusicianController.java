package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.Musician;
import com.bandrehearsal.bandrehearsalmanager.services.IMusicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("musician")
@CrossOrigin("*")
public class MusicianController {

    @Autowired
    private IMusicianService musicianService;

    @GetMapping("get-list")
    public List<Musician> getList() {
        return musicianService.findAll();
    }

    @GetMapping("get/{id}")
    public Musician getById(@PathVariable Long id) {
        return musicianService.findById(id);
    }

    @PostMapping("create")
    public Musician create(@RequestBody Musician musician) {
        return musicianService.save(musician);
    }

    @PutMapping("update")
    public Musician update(@RequestBody Musician musician) {
        return musicianService.save(musician);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        musicianService.deleteById(id);
    }
}
