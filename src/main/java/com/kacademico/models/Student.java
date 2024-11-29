package com.kacademico.models;

import java.io.Serializable;
import java.util.UUID;

import com.kacademico.enums.EShift;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "students")
@Entity
public class Student implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Relationships
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transcript_id", nullable = false)
    private Transcript transcript;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course; 

    // Simple Attributes
    @NotEmpty
    @Size(max=14, min=14, message="Matrícula deve conter 14 dígitos.")
    private String enrollment; // Year + Semester + CourseCode + ShiftCode + RandomNumbers = xxxx.x.xx.xxx.xxxx
    
    private EShift shift;
    
    // Constructor
    public Student(User user, Course course, String enrollment, EShift shift) {
        this.user = user;
        this.course = course;
        this.enrollment = enrollment;
        this.shift = shift;
        this.transcript = new Transcript(this);
    }

}
