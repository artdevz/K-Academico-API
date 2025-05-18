package com.kacademico.infra.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.values.Schedule;
import com.kacademico.domain.models.values.Timetable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grades")
public class GradeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Min(20)
    private int capacity;

    private int currentStudents;

    private EGrade status;

    @Embedded
    private Schedule schedule;

    @ElementCollection
    @CollectionTable(name = "grade_timetables", joinColumns = @JoinColumn(name = "grade_id"))
    private List<Timetable> timetables = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectEntity subject;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = true)
    private ProfessorEntity professor;

    @OneToMany(mappedBy = "grade", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<EnrolleeEntity> enrollees = new HashSet<>();

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ExamEntity> exams = new ArrayList<>();

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LessonEntity> lessons = new ArrayList<>();

}
