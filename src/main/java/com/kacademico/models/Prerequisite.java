// package com.kacademico.models;

// import java.io.Serializable;
// import java.util.UUID;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// @Getter
// @Setter
// @NoArgsConstructor
// @Table(name = "prerequisites")
// @Entity
// public class Prerequisite implements Serializable {
    
//     private static final long serialVersionUID = 1L;
//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO)
//     @Column(name = "id")
//     private UUID id;

//     @ManyToOne
//     @JoinColumn(name = "subject_id") 
//     private Subject subject;

//     @ManyToOne
//     @JoinColumn(name = "prerequisite_subject_id")
//     private Subject prerequisiteSubject;

//     public Prerequisite(Subject subject, Subject prerequisiteSubject) {
//         this.subject = subject;
//         this.prerequisiteSubject = prerequisiteSubject;
//     }

// }
