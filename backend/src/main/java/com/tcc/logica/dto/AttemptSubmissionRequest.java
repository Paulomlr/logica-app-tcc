package com.tcc.logica.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * {@code answers} is one list per row (same row order as the generated truth
 * table: row 0 = all variables true, counting down to all false), and within
 * each row, one boolean per fillable column, in the same left-to-right order
 * those columns appear in {@link ExercisePlayView#columnLabels()} once the
 * variable columns are skipped.
 */
public record AttemptSubmissionRequest(
        @NotEmpty List<List<Boolean>> answers,
        @Min(0) int timeSpentSeconds) {
}
