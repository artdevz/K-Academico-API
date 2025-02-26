package com.kacademic.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kacademic.enums.ESubject;

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
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;    

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "subject")
    private List<Grade> grades = new ArrayList<>();

    private ESubject type;

    private String name;
    
    private String description;
    
    @Min(40)
    @Max(80)
    private int duration;

    @Min(1)
    private int semester;

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
