package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Attendance;
import com.kacademico.infra.entities.AttendanceEntity;

public class AttendanceEntityMapper {

    public static Attendance toDomain(AttendanceEntity entity) {
        if (entity == null) return null;
        Attendance attendance = new Attendance(
            entity.getId(),
            entity.isAbsent(),
            EnrolleEntityMapper.toDomain(entity.getEnrollee(), false),
            LessonEntityMapper.toDomain(entity.getLesson(), false)
        );

        return attendance;
    }
    
    public static AttendanceEntity toEntity(Attendance attendance) {
        if (attendance == null) return null;
        AttendanceEntity entity = new AttendanceEntity();
        entity.setId(attendance.getId());
        entity.setAbsent(attendance.isAbsent());
        entity.setEnrollee(EnrolleEntityMapper.toEntity(attendance.getEnrollee()));
        entity.setLesson(LessonEntityMapper.toEntity(attendance.getLesson()));

        return entity;
    }
    
}
