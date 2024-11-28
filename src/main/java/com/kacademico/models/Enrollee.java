package com.kacademico.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.kacademico.enums.EEnrollee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @OneToOne
    @JoinColumn(name = "grade_id", referencedColumnName = "id", nullable = false)
    private Grade grade;

    private EEnrollee status;

    private int absences;

    @Max(10)
    private float avarage;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "enrollee_exam",
        joinColumns = @JoinColumn(name = "enrollee_id"),
        inverseJoinColumns = @JoinColumn(name = "exam_id")
    )
    private Set<Exam> exams = new HashSet<>();

    public Enrollee(Student student, Grade grade) {
        this.student = student;
        this.grade = grade;
        this.status = EEnrollee.ENROLLED;
        this.absences = 0;
        this.avarage = 0;
    }

}
