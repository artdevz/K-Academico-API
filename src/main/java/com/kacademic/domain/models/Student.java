package com.kacademic.domain.models;

import java.util.ArrayList;
import java.util.List;
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
    
    private int credits;

    private float average;

    @NotNull
    @Embedded
    private Enrollment enrollment;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course; 

    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Enrollee> enrollees = new ArrayList<>();
        
    public Student(String name, String email, String password, Set<Role> roles, Enrollment enrollment, Course course) {
        super(name, email, password, roles);
        this.credits = 0;
        this.average = 0; // Default Value = 0
        this.enrollment = enrollment;
        this.course = course;
    }

}