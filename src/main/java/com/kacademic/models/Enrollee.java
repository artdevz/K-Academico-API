package com.kacademic.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.kacademic.enums.EEnrollee;

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
    
    private static final long serialVersionUID = 1L;
    
    // Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "grade_id", referencedColumnName = "id", nullable = true)
    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "transcript_id", referencedColumnName = "id", nullable = false)
    private Transcript transcript;

    @OneToMany(mappedBy = "enrollee")
    private Set<Evaluation> evaluations = new HashSet<>();

    // Simple Attributes
    private int absences;
    
    @Max(10)
    private float avarage;
    
    private EEnrollee status;

    // Constructor
    public Enrollee(Student student, Transcript transcript, Grade grade) {
        this.student = student;
        this.transcript = transcript;
        this.grade = grade;
        this.status = EEnrollee.ENROLLED; // Default Status
        this.absences = 0; // Inicia-se com 0
        this.avarage = 0; // Inicia-se com 0
    }

}
