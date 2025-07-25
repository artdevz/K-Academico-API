package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Attendance;

public interface IAttendanceRepository {

    List<Attendance> findAll();
    Optional<Attendance> findById(UUID id);
    Attendance save(Attendance attendance);
    void deleteById(UUID id);

    boolean existsByEnrolleeIdAndLessonId(UUID enrolleeId, UUID lessonId);

}
