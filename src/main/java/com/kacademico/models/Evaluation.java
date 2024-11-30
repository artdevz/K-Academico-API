package com.kacademico.models;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "evaluations")
@Entity
public class Evaluation {
    
    // Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Relationships
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "enrollee_id", referencedColumnName = "id")
    private Enrollee enrollee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    private Exam exam;

    // Simple Attributes
    private float score;

    // Constructor
    public Evaluation(Enrollee enrollee, Exam exam, float score) {
        this.enrollee = enrollee;
        this.exam = exam;
        this.score = score;
    }

}
