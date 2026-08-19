package com.bandrehearsal.bandrehearsalmanager.controllers;

import com.bandrehearsal.bandrehearsalmanager.models.Attendance;
import com.bandrehearsal.bandrehearsalmanager.services.IAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("attendance")
@CrossOrigin("*")
public class AttendanceController {

    @Autowired
    private IAttendanceService attendanceService;

    @GetMapping("get-list")
    public List<Attendance> getList() {
        return attendanceService.findAll();
    }

    @GetMapping("get/{id}")
    public Attendance getById(@PathVariable Long id) {
        return attendanceService.findById(id);
    }

    @PostMapping("create")
    public Attendance create(@RequestBody Attendance attendance) {
        return attendanceService.save(attendance);
    }

    @PutMapping("update")
    public Attendance update(@RequestBody Attendance attendance) {
        return attendanceService.save(attendance);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        attendanceService.deleteById(id);
    }
}
