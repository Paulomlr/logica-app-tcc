package com.tcc.logica.exercise;

import java.util.List;
import java.util.Map;

/**
 * What the frontend needs to render a blank table. {@code columnIsFillable} is
 * parallel to {@code columnLabels}: false for a given variable column (already
 * shown via rowAssignments), true for a subexpression/result column the student
 * must fill in. Submitting an attempt sends one boolean per row per fillable
 * column, in the same left-to-right order those columns appear here — skipping
 * every column where columnIsFillable is false.
 */
public record ExercisePlayView(
        Long exerciseId,
        String formula,
        List<String> variables,
        List<String> columnLabels,
        List<Boolean> columnIsFillable,
        List<Map<String, Boolean>> rowAssignments) {
}
