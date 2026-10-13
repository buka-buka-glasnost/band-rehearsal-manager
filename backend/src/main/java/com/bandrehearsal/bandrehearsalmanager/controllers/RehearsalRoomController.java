package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.RehearsalRoom;
import com.bandrehearsal.bandrehearsalmanager.services.IRehearsalRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rehearsal-room")
public class RehearsalRoomController {

    private final IRehearsalRoomService rehearsalRoomService;

    public RehearsalRoomController(IRehearsalRoomService rehearsalRoomService) {
        this.rehearsalRoomService = rehearsalRoomService;
    }

    @GetMapping("/get-list")
    public List<RehearsalRoom> getList() {
        return rehearsalRoomService.findAll();
    }

    @GetMapping("/get/{id}")
    public RehearsalRoom getById(@PathVariable Long id) {
        return rehearsalRoomService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<RehearsalRoom> create(@Valid @RequestBody RehearsalRoom rehearsalRoom) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rehearsalRoomService.create(rehearsalRoom));
    }

    @PutMapping("/update")
    public RehearsalRoom update(@Valid @RequestBody RehearsalRoom rehearsalRoom) {
        return rehearsalRoomService.update(rehearsalRoom);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rehearsalRoomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
