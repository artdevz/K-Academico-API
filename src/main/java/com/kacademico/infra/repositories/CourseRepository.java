package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Course;
import com.kacademico.domain.repositories.ICourseRepository;
import com.kacademico.infra.entities.CourseEntity;
import com.kacademico.infra.mapper.CourseEntityMapper;
import com.kacademico.infra.repositories.jpa.CourseJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CourseRepository implements ICourseRepository {
    
    private final CourseJpaRepository jpa;

    @Override
    public List<Course> findAll() {
        return jpa.findAll().stream().map(entity -> CourseEntityMapper.toDomain(entity, false)).toList();
    }

    @Override
    public Optional<Course> findById(UUID id) {
        return jpa.findById(id).map(entity -> CourseEntityMapper.toDomain(entity, true));
    }
    
    @Override
    public Course save(Course course) {
        System.out.println("Salvando Course");
        CourseEntity entity = CourseEntityMapper.toEntity(course);
        CourseEntity saved = jpa.save(entity);
        System.out.println("Salvo");
        return CourseEntityMapper.toDomain(saved, true);
    }
    
    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
    
    @Override
    public Optional<Course> findByCode(String code) {
        return jpa.findByCode(code).map(entity -> CourseEntityMapper.toDomain(entity, true));
    }

    @Override
    public Optional<Course> findByName(String name) {
        return jpa.findByName(name).map(entity -> CourseEntityMapper.toDomain(entity, true));
    }

    @Override
    public Optional<Course> findWithSubjectsById(UUID id) {
        return jpa.findWithSubjectsById(id).map(entity -> CourseEntityMapper.toDomain(entity, true));
    }

}
