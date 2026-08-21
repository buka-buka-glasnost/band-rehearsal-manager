package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.Band;
import com.bandrehearsal.bandrehearsalmanager.services.IBandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("band")
@CrossOrigin("*")
public class BandController {

    @Autowired
    private IBandService bandService;

    @GetMapping("get-list")
    public List<Band> getlist() {
        return bandService.findAll();
    }

    @GetMapping("get/{id}")
    public Band getById(@PathVariable Long id) {
        return bandService.findById(id);
    }

    @PostMapping("create")
    public Band create(@RequestBody Band band) {
        return bandService.save(band);
    }

    @PutMapping("update")
    public Band update(@RequestBody Band band) {
        return bandService.save(band);
    }

    @DeleteMapping("delete/{id}")
    public void delete (@PathVariable Long id) {
        bandService.deleteById(id);
    }
}
