package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.Rehearsal;
import com.bandrehearsal.bandrehearsalmanager.services.IRehearsalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("rehearsal")
@CrossOrigin("*")
public class RehearsalController {

    @Autowired
    private IRehearsalService rehearsalService;

    @GetMapping("get-list")
    public List<Rehearsal> getList() {
        return rehearsalService.findAll();
    }
    @GetMapping("get/{id}")
    public Rehearsal getById(@PathVariable Long id) {
        return rehearsalService.findById(id);
    }

    @PostMapping("create")
    public Rehearsal create(@RequestBody Rehearsal rehearsal) {
        return rehearsalService.save(rehearsal);
    }

    @PutMapping("update")
    public Rehearsal update(@RequestBody Rehearsal rehearsal) {
        return rehearsalService.save(rehearsal);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        rehearsalService.deleteById(id);
    }
}
