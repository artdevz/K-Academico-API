package com.kacademic.domain.models;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "evaluations")
@Entity
public class Evaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "enrollee_id", referencedColumnName = "id")
    private Enrollee enrollee;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    private Exam exam;

    @Min(0)
    @Max(10)
    private float score;

    public Evaluation(Enrollee enrollee, Exam exam, float score) {
        this.enrollee = enrollee;
        this.exam = exam;
        this.score = score;
    }

}
