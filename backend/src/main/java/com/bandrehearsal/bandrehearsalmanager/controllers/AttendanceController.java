package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.Attendance;
import com.bandrehearsal.bandrehearsalmanager.services.IAttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final IAttendanceService attendanceService;

    public AttendanceController(IAttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/get-list")
    public List<Attendance> getList() {
        return attendanceService.findAll();
    }

    @GetMapping("/get/{id}")
    public Attendance getById(@PathVariable Long id) {
        return attendanceService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Attendance> create(@Valid @RequestBody Attendance attendance) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.create(attendance));
    }

    @PutMapping("/update")
    public Attendance update(@Valid @RequestBody Attendance attendance) {
        return attendanceService.update(attendance);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attendanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
