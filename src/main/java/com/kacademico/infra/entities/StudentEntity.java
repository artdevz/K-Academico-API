package com.kacademico.infra.entities;

import java.util.ArrayList;
import java.util.List;

import com.kacademico.infra.embeddables.EnrollmentEmbeddable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Student")
@Table(name = "students")
public class StudentEntity extends UserEntity {
    
    private Integer credits;

    private Float average;

    @NotNull
    @Embedded
    private EnrollmentEmbeddable enrollment;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course; 

    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EnrolleeEntity> enrollees = new ArrayList<>();

}
