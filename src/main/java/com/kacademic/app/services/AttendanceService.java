package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.attendance.AttendanceRequestDTO;
import com.kacademic.app.dto.attendance.AttendanceResponseDTO;
import com.kacademic.app.dto.attendance.AttendanceUpdateDTO;
import com.kacademic.domain.models.Attendance;
import com.kacademic.domain.repositories.AttendanceRepository;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.LessonRepository;

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
    
    @Async
    public CompletableFuture<String> createAsync(AttendanceRequestDTO data) {

        Attendance attendance = new Attendance(
            enrolleeR.findById(data.enrollee()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found")),
            lessonR.findById(data.lesson()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found")),
            data.isAbsent()
        );
        
        attendanceR.save(attendance);
        return CompletableFuture.completedFuture("Created " + entity);

    }

    @Async
    public CompletableFuture<List<AttendanceResponseDTO>> readAllAsync() {

        return CompletableFuture.completedFuture(
            attendanceR.findAll().stream()
            .map(attendance -> new AttendanceResponseDTO(
                attendance.getId(),
                attendance.getEnrollee().getId(),
                attendance.getLesson().getId(),
                attendance.isAbsent()
            ))
            .collect(Collectors.toList()));

    }

    @Async
    public CompletableFuture<AttendanceResponseDTO> readByIdAsync(UUID id) {

        Attendance attendance = attendanceR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
        
        return CompletableFuture.completedFuture(
            new AttendanceResponseDTO(
                attendance.getId(),
                attendance.getEnrollee().getId(),
                attendance.getLesson().getId(),
                attendance.isAbsent()
    ));

    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, AttendanceUpdateDTO data) {

        Attendance attendance = attendanceR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
            
        attendanceR.save(attendance);
        return CompletableFuture.completedFuture("Updated " + entity);
        
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {

        if (!attendanceR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found");
        
        attendanceR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted " + entity);

    }

}
