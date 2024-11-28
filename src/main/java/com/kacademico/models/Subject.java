package com.kacademico.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kacademico.enums.ESubject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="subjects")
@Entity
public class Subject implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;    

    // Relationships
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "subject")
    private List<Grade> grades = new ArrayList<>();

    // Simple Attributes
    private ESubject type;

    private String name;
    
    private String description;
    
    @Min(40)
    @Max(80)
    private int duration;

    @Min(1)
    private int semester;

    // Collections
    private List<UUID> prerequisites;

    // private List<Subject> corequisites;

    // Constructor
    public Subject(Course course, String name, String description, int duration, int semester, List<UUID> prerequisites) {
        this.course = course;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.semester = semester;        
        this.prerequisites = prerequisites;
    }    

}
