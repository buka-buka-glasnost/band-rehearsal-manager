package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.RehearsalRoom;
import com.bandrehearsal.bandrehearsalmanager.services.IRehearsalRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("rehearsal-room")
@CrossOrigin("*")
public class RehearsalRoomController {

    @Autowired
    private IRehearsalRoomService rehearsalRoomService;

    @GetMapping("get-list")
    public List<RehearsalRoom> getList() {
        return rehearsalRoomService.findAll();
    }

    @GetMapping("get/{id}")
    public RehearsalRoom getById(@PathVariable Long id) {
        return rehearsalRoomService.findById(id);
    }

    @PostMapping("create")
    public RehearsalRoom create (@RequestBody RehearsalRoom rehearsalRoom) {
        return rehearsalRoomService.save(rehearsalRoom);
    }

    @PutMapping("update")
    public RehearsalRoom update(@RequestBody RehearsalRoom rehearsalRoom) {
        return rehearsalRoomService.save(rehearsalRoom);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        rehearsalRoomService.deleteById((id));
    }
}
