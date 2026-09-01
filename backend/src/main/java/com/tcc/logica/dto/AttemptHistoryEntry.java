package com.tcc.logica.dto;

import com.tcc.logica.model.Difficulty;
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
