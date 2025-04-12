package com.kacademic.domain.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "exams")
@Entity
public class Exam implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "exam")
    private Set<Evaluation> evaluations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @Size(min = 3, max=16, message = "Exam name must be between 3 and 16 characters")
    private String name;

    private int maximum;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public Exam(Grade grade, String name, int maximum, LocalDate date) {
        this.grade = grade;
        this.name = name;        
        this.maximum = maximum;
        this.date = date;
    }    

}
