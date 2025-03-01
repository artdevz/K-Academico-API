package com.kacademic.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.attendance.AttendanceRequestDTO;
import com.kacademic.dto.attendance.AttendanceResponseDTO;
import com.kacademic.models.Attendance;
import com.kacademic.repositories.AttendanceRepository;

@Service
public class AttendanceService {
    
    private final AttendanceRepository attendanceR;
    
    private final MappingService mapS;

    public AttendanceService(AttendanceRepository attendanceR, MappingService mapS) {
        this.attendanceR = attendanceR;
        this.mapS = mapS;
    }
    
    public void create(AttendanceRequestDTO data) {

        Attendance attendance = new Attendance(
            mapS.findEnrolleeById(data.enrollee()),
            mapS.findLessonById(data.lesson()),
            data.isAbsent()
        );
        
        attendanceR.save(attendance);

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

    public Attendance update(UUID id, Map<String, Object> fields) {

        Optional<Attendance> existingEvalution = attendanceR.findById(id);
    
        if (existingEvalution.isPresent()) {
            Attendance attendance = existingEvalution.get();            
    
            fields.forEach((key, value) -> {
                switch (key) {
                    
                    default:
                        Field field = ReflectionUtils.findField(Attendance.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, attendance, value);
                        }
                        break;
                }
            });
            
            return attendanceR.save(attendance);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not Found.");
        
    }

    public void delete(UUID id) {

        if (!attendanceR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not Found.");
        attendanceR.deleteById(id);

    }

}
