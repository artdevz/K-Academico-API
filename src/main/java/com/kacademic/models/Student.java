package com.kacademic.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transcript_id", nullable = false)
    private Transcript transcript;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course; 

    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Enrollee> enrollees = new HashSet<>();

    @NotEmpty
    @Size(max=14, min=14, message="Matrícula deve conter 14 dígitos.")
    private String enrollment; // Year + Semester + CourseCode + ShiftCode + RandomNumbers = xxxx.x.xx.xxx.xxxx

    private float average;
        
    public Student(String name, String email, String password, Course course, String enrollment) {
        super(name, email, password);
        this.course = course;
        this.enrollment = enrollment;
        this.average = 0; // Inicia-se com 0
        this.transcript = new Transcript(this);
    }

}
