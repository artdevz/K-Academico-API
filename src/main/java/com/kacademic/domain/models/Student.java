package com.kacademic.domain.models;

import java.util.HashSet;
import java.util.Set;

import com.kacademic.domain.models.values.Enrollment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "students")
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course; 

    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Enrollee> enrollees = new HashSet<>();

    @NotNull
    @Embedded
    private Enrollment enrollment;

    private float average;
        
    public Student(String name, String email, String password, Set<Role> roles, Course course, Enrollment enrollment) {
        super(name, email, password, roles);
        this.course = course;
        this.enrollment = enrollment;
        this.average = 0; // Inicia-se com 0
    }

}