package com.kacademico.domain.models;

import java.util.UUID;

public class Evaluation {

    private static final float MIN_SCORE = 0f;
    
    private UUID id;
    private float score;

    private Enrollee enrollee;
    private Exam exam;

    public Evaluation() {};

    public Evaluation(UUID id, float score, Enrollee enrollee, Exam exam) {
        this.id = id;
        setScore(score);
        this.enrollee = enrollee;
        this.exam = exam;
    }

    public UUID getId() { return id; }
    public float getScore() { return score; }
    public Enrollee getEnrollee() { return enrollee; }
    public Exam getExam() { return exam; }

    public void setScore(float score) {
        if (score < MIN_SCORE || score > exam.getMaximum()) throw new IllegalArgumentException("Score must be between " + MIN_SCORE + " and " + exam.getMaximum());
        this.score = score;
    }

}
