package com.kacademico.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "exams")
@Entity
public class Exam implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    private String name;

    private float score;

    private int maximum;
    
    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @ManyToMany(mappedBy = "exams", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private Set<Enrollee> enrollees = new HashSet<>();

    public Exam(String name, int maximum, LocalDate date, Grade grade, Set<Enrollee> enrollees) {
        this.name = name;
        this.score = 0;
        this.maximum = maximum;
        this.date = date;
        this.grade = grade;
        this.enrollees = enrollees != null ? enrollees : new HashSet<>();
    }    

}
