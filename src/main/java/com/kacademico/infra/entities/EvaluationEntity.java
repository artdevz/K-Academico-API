package com.kacademico.infra.entities;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "evaluations")
public class EvaluationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private float score;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "enrollee_id", referencedColumnName = "id")
    private EnrolleeEntity enrollee;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    private ExamEntity exam;

}
