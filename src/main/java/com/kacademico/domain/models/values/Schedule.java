package com.kacademico.domain.models.values;

import java.util.Objects;

public class Schedule {
    
    private String semester;
    private String locate;

    public Schedule() {}

    public Schedule(String semester, String locate) {
        setSemester(semester);
        setLocate(locate);
    }

    public String getSemester() { return semester; }
    public String getLocate() { return locate; }

    public void setSemester(String semester) { this.semester = semester; }
    public void setLocate(String locate) { this.locate = locate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule that = (Schedule) o;
        return (
            Objects.equals(semester, that.semester) &&
            Objects.equals(locate, that.locate)
        );
    }

    @Override
    public int hashCode() { return Objects.hash(semester, locate); }

}
