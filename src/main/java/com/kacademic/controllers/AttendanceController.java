package com.kacademic.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.dto.attendance.AttendanceRequestDTO;
import com.kacademic.dto.attendance.AttendanceResponseDTO;
import com.kacademic.services.AttendanceService;

import jakarta.validation.Valid;

@RequestMapping("/attendance")
@RestController
public class AttendanceController {
    
    private final AttendanceService attendanceS;

    public AttendanceController(AttendanceService attendanceS) {
        this.attendanceS = attendanceS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid AttendanceRequestDTO request) {

        attendanceS.create(request);

        return new ResponseEntity<>("Created Attendance.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<AttendanceResponseDTO>> readAll() {

        return new ResponseEntity<>(attendanceS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponseDTO> readById(@PathVariable UUID id) {

        return new ResponseEntity<>(attendanceS.readById(id), HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        attendanceS.update(id, fields);
        
        return new ResponseEntity<>("Updated Attendance", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        attendanceS.delete(id);

        return new ResponseEntity<>("Deleted Attendance", HttpStatus.OK);
           
    }

}
