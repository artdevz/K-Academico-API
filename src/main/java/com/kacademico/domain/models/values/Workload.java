package com.kacademico.domain.models.values;

public class Workload {
    
    private int mandatoryHours;
    private int electiveHours;
    private int complementaryHours;
    private int internshipHours; // Estágio
    private int totalHours;

    public Workload() {}

    public Workload(int mandatoryHours, int electiveHours, int complementaryHours, int internshipHours) {        
        setMandatoryHours(mandatoryHours);
        setElectiveHours(electiveHours);
        setComplementaryHours(complementaryHours);
        setInternalshipHours(internshipHours);
        setTotalHours(mandatoryHours + electiveHours + complementaryHours + internshipHours);
    }

    public int getMandatoryHours() { return mandatoryHours; }
    public int getElectiveHours() { return electiveHours; }
    public int getComplementaryHours() { return complementaryHours; }
    public int getInternshipHours() { return internshipHours; }
    public int getTotalHours() { return totalHours; }
    
    public void setMandatoryHours(int mandatoryHours) { 
        if (mandatoryHours < 0) throw new IllegalArgumentException("Mandatory hours must be non-negative");
        this.mandatoryHours = mandatoryHours; 
    }

    public void setElectiveHours(int electiveHours) {
        if (electiveHours < 0) throw new IllegalArgumentException("Elective hours must be non-negative");
        this.electiveHours = electiveHours;
    }
    
    public void setComplementaryHours(int complementaryHours) { 
        if (complementaryHours < 0) throw new IllegalArgumentException("Complementary hours must be non-negative");
        this.complementaryHours = complementaryHours; 
    }
    
    public void setInternalshipHours(int internshipHours) { 
        if (internshipHours < 0) throw new IllegalArgumentException("Internship hours must be non-negative");
        this.internshipHours = internshipHours; 
    }
    
    public void setTotalHours(int totalHours) { 
        if (totalHours < 0) throw new IllegalArgumentException("Total hours must be non-negative");
        this.totalHours = totalHours; 
    }

}
