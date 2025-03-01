package com.kacademic.models;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "enrollee_id", referencedColumnName = "id")
    private Enrollee enrollee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    private Lesson lesson;

    private boolean isAbsent;

    public Attendance(Enrollee enrollee, Lesson lesson, boolean isAbsent) {
        this.enrollee = enrollee;
        this.lesson = lesson;
        this.isAbsent = isAbsent;
    }

}
