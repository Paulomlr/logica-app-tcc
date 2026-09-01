package com.tcc.logica.dto;

import com.tcc.logica.model.Difficulty;
import java.util.Map;

public record ProgressSummary(
        long totalAttempts,
        long correctAttempts,
        long incorrectAttempts,
        double accuracyRate,
        long totalTimeSpentSeconds,
        Map<Difficulty, DifficultyBreakdown> byDifficulty) {

    public record DifficultyBreakdown(long totalAttempts, long correctAttempts, double accuracyRate) {
    }
}
