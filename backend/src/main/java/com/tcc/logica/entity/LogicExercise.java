package com.tcc.logica.entity;

import com.tcc.logica.model.Difficulty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "logic_exercise")
public class LogicExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String formula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected LogicExercise() {
    }

    public LogicExercise(String formula, Difficulty difficulty) {
        this.formula = formula;
        this.difficulty = difficulty;
    }

    public Long getId() {
        return id;
    }

    public String getFormula() {
        return formula;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
