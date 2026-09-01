package com.tcc.logica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "exercise_attempt")
public class ExerciseAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private LogicExercise exercise;

    @Column(nullable = false)
    private boolean correct;

    @Column(name = "time_spent_seconds", nullable = false)
    private int timeSpentSeconds;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt = Instant.now();

    protected ExerciseAttempt() {
    }

    public ExerciseAttempt(AppUser user, LogicExercise exercise, boolean correct, int timeSpentSeconds) {
        this.user = user;
        this.exercise = exercise;
        this.correct = correct;
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public LogicExercise getExercise() {
        return exercise;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }
}
