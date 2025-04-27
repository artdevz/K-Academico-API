package com.kacademico.domain.models;

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
@Table(name = "attendances")
@Entity
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private boolean isAbsent;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "enrollee_id", referencedColumnName = "id")
    private Enrollee enrollee;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    private Lesson lesson;

    public Attendance(boolean isAbsent, Enrollee enrollee, Lesson lesson) {
        this.isAbsent = isAbsent;
        this.enrollee = enrollee;
        this.lesson = lesson;
    }

}
