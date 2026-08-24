package com.tcc.logica.exercise;

import java.time.Instant;

public record AttemptHistoryEntry(
        Long attemptId,
        Long exerciseId,
        String formula,
        Difficulty difficulty,
        boolean correct,
        int timeSpentSeconds,
        Instant submittedAt) {
}
