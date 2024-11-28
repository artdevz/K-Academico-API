package com.kacademico.models;

import java.io.Serializable;
import java.util.UUID;

import com.kacademico.enums.EShift;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @NotEmpty
    @Size(max=14, min=14, message="Matrícula deve conter 14 dígitos.")
    private String enrollment; // Year + Semester + CourseCode + ShiftCode + RandomNumbers = xxxx.x.xx.xxx.xxxx
    
    private EShift shift;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;    
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transcript_id", nullable = false)
    private Transcript transcript;
    
    public Student(User user, String enrollment, Course course, EShift shift) {
        this.user = user;
        this.enrollment = enrollment;
        this.course = course;
        this.shift = shift;
    }

}
