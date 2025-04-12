package com.kacademic.domain.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.kacademic.domain.enums.EGrade;
import com.kacademic.domain.models.values.Timetable;
import com.kacademic.shared.utils.Semester;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "grades")
@Entity
public class Grade implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = true)
    private Professor professor;

    @OneToMany(mappedBy = "grade", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Enrollee> enrollees = new HashSet<>();

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>();

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @Min(20)
    private int capacity;

    private int currentStudents;

    @Semester
    private String semester;

    private String locate;

    private EGrade status;

    @ElementCollection
    @CollectionTable(name = "grade_timetables", joinColumns = @JoinColumn(name = "grade_id"))
    private List<Timetable> timetables = new ArrayList<>();

    public Grade(Subject subject, Professor professor, int capacity, String semester, String locate, List<Timetable> timetables) {
        this.subject = subject;
        this.professor = professor;
        this.capacity = capacity;
        this.currentStudents = 0; // Inicia-se em 0; Irá atulizar a medida que é adicionado novos Estudantes na Turma.
        this.semester = semester;
        this.locate = locate;
        this.timetables = timetables;
        this.status = EGrade.PENDING;
    }    

}
