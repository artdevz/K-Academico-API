package com.kacademico.infra.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.kacademico.domain.enums.EEnrollee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollees")
public class EnrolleeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int absences;
    
    private float average;
    
    private EEnrollee status;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "grade_id", referencedColumnName = "id", nullable = true)
    private GradeEntity grade;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private StudentEntity student;

    @OneToMany(mappedBy = "enrollee")
    private Set<EvaluationEntity> evaluations = new HashSet<>();

    @OneToMany(mappedBy = "enrollee")
    private Set<AttendanceEntity> attendances = new HashSet<>();

}
