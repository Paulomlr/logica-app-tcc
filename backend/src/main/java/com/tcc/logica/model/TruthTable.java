package com.tcc.logica.model;

import java.util.List;

/**
 * @param variables    distinct variables, alphabetically sorted
 * @param columnLabels one label per subexpression column, in evaluation order
 *                     (variables and intermediate subformulas first, full formula last)
 * @param rows         one row per possible truth assignment (2^variables.size())
 */
public record TruthTable(List<String> variables, List<String> columnLabels, List<TruthTableRow> rows) {
}
