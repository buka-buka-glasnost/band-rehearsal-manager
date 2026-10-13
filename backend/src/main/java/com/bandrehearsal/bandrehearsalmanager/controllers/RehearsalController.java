package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.Rehearsal;
import com.bandrehearsal.bandrehearsalmanager.services.IRehearsalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rehearsal")
public class RehearsalController {

    private final IRehearsalService rehearsalService;

    public RehearsalController(IRehearsalService rehearsalService) {
        this.rehearsalService = rehearsalService;
    }

    @GetMapping("/get-list")
    public List<Rehearsal> getList() {
        return rehearsalService.findAll();
    }

    @GetMapping("/get/{id}")
    public Rehearsal getById(@PathVariable Long id) {
        return rehearsalService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Rehearsal> create(@Valid @RequestBody Rehearsal rehearsal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rehearsalService.create(rehearsal));
    }

    @PutMapping("/update")
    public Rehearsal update(@Valid @RequestBody Rehearsal rehearsal) {
        return rehearsalService.update(rehearsal);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rehearsalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
