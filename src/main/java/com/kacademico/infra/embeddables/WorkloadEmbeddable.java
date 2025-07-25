package com.kacademico.infra.embeddables;

import com.kacademico.domain.models.values.Workload;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class WorkloadEmbeddable {
    
    private int mandatoryHours;

    private int electiveHours;
    
    private int complementaryHours;
    
    private int internshipHours;
    
    private int totalHours;

    public WorkloadEmbeddable(int mandatoryHours, int electiveHours, int complementaryHours, int internshipHours) {
        this.mandatoryHours = mandatoryHours;
        this.electiveHours = electiveHours;
        this.complementaryHours = complementaryHours;
        this.internshipHours = internshipHours;
        this.totalHours = mandatoryHours + electiveHours + complementaryHours + internshipHours;
    }

    public Workload toDomain() { return new Workload(this.mandatoryHours, this.electiveHours, this.complementaryHours, this.internshipHours); }
    public static WorkloadEmbeddable fromDomain(Workload workload) { return new WorkloadEmbeddable(workload.getMandatoryHours(), workload.getElectiveHours(), workload.getComplementaryHours(), workload.getInternshipHours()); }

}
