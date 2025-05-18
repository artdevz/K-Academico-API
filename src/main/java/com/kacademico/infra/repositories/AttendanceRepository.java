package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Attendance;
import com.kacademico.domain.repositories.IAttendanceRepository;
import com.kacademico.infra.repositories.jpa.AttendanceJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class AttendanceRepository implements IAttendanceRepository {
    
    private final AttendanceJpaRepository jpa;

    @Override
    public List<Attendance> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Attendance> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Attendance save(Attendance attendance) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByEnrolleeIdAndLessonId(UUID enrolleeId, UUID lessonId) {
        throw new UnsupportedOperationException("Unimplemented method 'existsByEnrolleeIdAndLessonId'");
    }

}
