package com.kacademico.domain.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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

    @Size(max=128)
    private String name;
    
    @Size(max=255)
    private String description;
    
    @Min(40)
    @Max(80)
    private int duration;

    @Min(1)
    private int semester;

    private boolean isRequired;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "subject")
    private List<Grade> grades = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
        name = "subject_prerequisite_groups",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisites_id")
    )
    private List<Equivalence> prerequisites = new ArrayList<>();

    public Subject(String name, String description, int duration, int semester, boolean isRequired, Course course, List<Equivalence> prerequisites) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.semester = semester;
        this.isRequired = isRequired;        
        this.course = course;
        this.prerequisites = prerequisites;
    }    

}
