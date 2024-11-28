package com.kacademico.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.kacademico.utils.Timetable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
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
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Min(1)
    private int capacity;

    private int numberOfStudents;

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Enrollee> enrollees = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "grade_timetables", joinColumns = @JoinColumn(name = "grade_id"))
    private List<Timetable> timetables;

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>();

    private String locate;

    public Grade(Subject subject, Professor professor, int capacity, List<Timetable> timetables, String locate) {
        this.subject = subject;
        this.professor = professor;
        this.capacity = capacity;
        this.timetables = timetables;
        this.locate = locate;
        this.numberOfStudents = 0;
    }    

}
