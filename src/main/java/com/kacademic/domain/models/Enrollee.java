package com.kacademic.domain.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.kacademic.domain.enums.EEnrollee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "enrollees")
@Entity
public class Enrollee implements Serializable {
        
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int absences;
    
    @Max(10)
    private float average;
    
    private EEnrollee status; // Student Status (Enrolled, APPROVED, FINAL_EXAM, FAILED) 

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "grade_id", referencedColumnName = "id", nullable = true)
    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @OneToMany(mappedBy = "enrollee")
    private Set<Evaluation> evaluations = new HashSet<>();

    @OneToMany(mappedBy = "enrollee")
    private Set<Attendance> attendances = new HashSet<>();

    public Enrollee(Grade grade, Student student) {
        this.absences = 0; // Default Value = 0
        this.average = 0; // Default Value = 0
        this.status = EEnrollee.ENROLLED; // Default Status
        this.grade = grade;
        this.student = student;
    }

}
