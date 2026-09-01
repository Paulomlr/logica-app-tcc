package com.tcc.logica.model;

import java.util.List;
import java.util.Map;

/**
 * One row of the table: the variable assignment for this row, and the
 * evaluated value of every column (in the same order as {@link TruthTable#columnLabels()}),
 * ending with the full formula's result as the last entry.
 */
public record TruthTableRow(Map<String, Boolean> assignment, List<Boolean> columnValues) {

    public boolean result() {
        return columnValues.get(columnValues.size() - 1);
    }
}
