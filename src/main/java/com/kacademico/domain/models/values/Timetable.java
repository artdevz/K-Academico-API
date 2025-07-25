package com.kacademico.domain.models.values;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

public class Timetable {
    
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    public Timetable() {}

    public Timetable(DayOfWeek day, LocalTime startTime, LocalTime endTime) {

    }

    public DayOfWeek getDay() { return day; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    public void setDay(DayOfWeek day) { this.day = day; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public boolean conflictWith(Timetable other) {
        if (!this.day.equals(other.day)) return false;

        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timetable)) return false;
        Timetable that = (Timetable) o;
        return (day == that.day &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime)
        );
    }

    @Override
    public int hashCode() { return Objects.hash(day, startTime, endTime); }

}
