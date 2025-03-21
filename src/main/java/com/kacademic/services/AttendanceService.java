package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.attendance.AttendanceRequestDTO;
import com.kacademic.dto.attendance.AttendanceResponseDTO;
import com.kacademic.dto.attendance.AttendanceUpdateDTO;
import com.kacademic.models.Attendance;
import com.kacademic.repositories.AttendanceRepository;
import com.kacademic.repositories.EnrolleeRepository;
import com.kacademic.repositories.LessonRepository;

@Service
public class AttendanceService {
    
    private final AttendanceRepository attendanceR;
    private final EnrolleeRepository enrolleeR;
    private final LessonRepository lessonR;
    
    private final String entity = "Attendance";

    public AttendanceService(AttendanceRepository attendanceR, EnrolleeRepository enrolleeR, LessonRepository lessonR) {
        this.attendanceR = attendanceR;
        this.enrolleeR = enrolleeR;
        this.lessonR = lessonR;
    }
    
    public String create(AttendanceRequestDTO data) {

        Attendance attendance = new Attendance(
            enrolleeR.findById(data.enrollee()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee")),
            lessonR.findById(data.lesson()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson")),
            data.isAbsent()
        );
        
        attendanceR.save(attendance);
        return "Created " + entity;

    }

    public List<AttendanceResponseDTO> readAll() {

        return attendanceR.findAll().stream()
            .map(attendance -> new AttendanceResponseDTO(
                attendance.getId(),
                attendance.getEnrollee().getId(),
                attendance.getLesson().getId(),
                attendance.isAbsent()
            ))
            .collect(Collectors.toList());

    }

    public AttendanceResponseDTO readById(UUID id) {

        Attendance attendance = attendanceR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not Found."));
        
        return new AttendanceResponseDTO(
            attendance.getId(),
            attendance.getEnrollee().getId(),
            attendance.getLesson().getId(),
            attendance.isAbsent()
    );

    }

    public String update(UUID id, AttendanceUpdateDTO data) {

        Attendance attendance = attendanceR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
            
        attendanceR.save(attendance);
        return "Updated " + entity;
        
    }

    public String delete(UUID id) {

        if (!attendanceR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not Found.");
        
        attendanceR.deleteById(id);
        return "Deleted " + entity;

    }

}
