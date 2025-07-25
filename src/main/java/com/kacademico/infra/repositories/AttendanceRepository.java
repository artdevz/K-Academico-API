package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Attendance;
import com.kacademico.domain.repositories.IAttendanceRepository;
import com.kacademico.infra.entities.AttendanceEntity;
import com.kacademico.infra.mapper.AttendanceEntityMapper;
import com.kacademico.infra.repositories.jpa.AttendanceJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class AttendanceRepository implements IAttendanceRepository {
    
    private final AttendanceJpaRepository jpa;

    @Override
    public List<Attendance> findAll() {
        return jpa.findAll().stream().map(AttendanceEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Attendance> findById(UUID id) {
        return jpa.findById(id).map(AttendanceEntityMapper::toDomain);
    }

    @Override
    public Attendance save(Attendance attendance) {
        AttendanceEntity entity = AttendanceEntityMapper.toEntity(attendance);
        AttendanceEntity saved = jpa.save(entity);

        return AttendanceEntityMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByEnrolleeIdAndLessonId(UUID enrolleeId, UUID lessonId) {
        return jpa.existsByEnrolleeIdAndLessonId(enrolleeId, lessonId);
    }

}
