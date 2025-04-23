package com.kacademic.domain.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kacademic.domain.enums.ELesson;

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
@Table(name = "lessons")
@Entity
public class Lesson implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max=32)
    private String topic;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private ELesson status; // (UPCOMING, PENDING)

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "grade.id", nullable = false)
    private Grade grade;

    @OneToMany(mappedBy = "lesson")
    private Set<Attendance> attendances = new HashSet<>();

    public Lesson(String topic, LocalDate date, Grade grade) {
        this.topic = topic;
        this.date = date;
        this.status = ELesson.UPCOMING; // Default Status
        this.grade = grade;
    }

}
