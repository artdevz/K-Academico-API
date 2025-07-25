package com.kacademico.domain.models;

import java.util.UUID;

public class Attendance {

    private UUID id;
    private boolean isAbsent;

    private Enrollee enrollee;
    private Lesson lesson;

    public Attendance() {};

    public Attendance(UUID id, boolean isAbsent, Enrollee enrollee, Lesson lesson) {
        validateSameGrade(enrollee, lesson);

        this.id = id;
        setAbsent(isAbsent);
        this.enrollee = enrollee;
        this.lesson = lesson;
    }

    public UUID getId() { return id; }
    public boolean isAbsent() { return isAbsent; }
    public Enrollee getEnrollee() { return enrollee; }
    public Lesson getLesson() { return lesson; }

    public void setAbsent(boolean isAbsent) { this.isAbsent = isAbsent; }

    private void validateSameGrade(Enrollee enrollee, Lesson lesson) { // 422
        if ( !( enrollee.getGrade().getId().equals(lesson.getGrade().getId()) ) ) 
            throw new IllegalArgumentException(String.format("Enrollee (%s) and Lesson (%s) must belong to the same Grade", enrollee.getId(), lesson.getId()));
    }

}
