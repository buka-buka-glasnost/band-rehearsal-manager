package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.Band;
import com.bandrehearsal.bandrehearsalmanager.services.IBandService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/band")
public class BandController {

    private final IBandService bandService;

    public BandController(IBandService bandService) {
        this.bandService = bandService;
    }

    @GetMapping("/get-list")
    public List<Band> getList() {
        return bandService.findAll();
    }

    @GetMapping("/get/{id}")
    public Band getById(@PathVariable Long id) {
        return bandService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Band> create(@Valid @RequestBody Band band) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bandService.create(band));
    }

    @PutMapping("/update")
    public Band update(@Valid @RequestBody Band band) {
        return bandService.update(band);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
