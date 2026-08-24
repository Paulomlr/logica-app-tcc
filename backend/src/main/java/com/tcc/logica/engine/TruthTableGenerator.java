package com.tcc.logica.engine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generates a full truth table for a formula, with one column per unique
 * subexpression (not just the final result) so a student can follow the
 * evaluation step by step, the same way it's done by hand in class.
 */
public final class TruthTableGenerator {

    private static final int MAX_VARIABLES = 6;

    private TruthTableGenerator() {
    }

    public static TruthTable generate(Expr formula) {
        Set<String> variableSet = new LinkedHashSet<>();
        collectVariables(formula, variableSet);
        List<String> variables = variableSet.stream().sorted().toList();

        if (variables.size() > MAX_VARIABLES) {
            throw new FormulaSyntaxException(
                    "Máximo de " + MAX_VARIABLES + " variáveis suportadas por tabela.", 0);
        }

        Set<Expr> columns = new LinkedHashSet<>();
        collectSubexpressions(formula, columns);
        List<Expr> columnExprs = new ArrayList<>(columns);
        List<String> columnLabels = columnExprs.stream().map(ExprFormatter::format).toList();

        int variableCount = variables.size();
        int totalRows = 1 << variableCount;
        List<TruthTableRow> rows = new ArrayList<>(totalRows);

        for (int r = 0; r < totalRows; r++) {
            Map<String, Boolean> assignment = new LinkedHashMap<>();
            for (int i = 0; i < variableCount; i++) {
                // row 0 => all true (V...V), counting down to row (2^n - 1) => all false.
                boolean value = ((r >> (variableCount - 1 - i)) & 1) == 0;
                assignment.put(variables.get(i), value);
            }
            List<Boolean> columnValues = columnExprs.stream()
                    .map(expr -> FormulaEvaluator.evaluate(expr, assignment))
                    .toList();
            rows.add(new TruthTableRow(assignment, columnValues));
        }

        return new TruthTable(variables, columnLabels, rows);
    }

    private static void collectVariables(Expr expr, Set<String> out) {
        switch (expr) {
            case Expr.Var v -> out.add(v.name());
            case Expr.Not n -> collectVariables(n.operand(), out);
            case Expr.Binary b -> {
                collectVariables(b.left(), out);
                collectVariables(b.right(), out);
            }
        }
    }

    private static void collectSubexpressions(Expr expr, Set<Expr> out) {
        switch (expr) {
            case Expr.Var v -> out.add(v);
            case Expr.Not n -> {
                collectSubexpressions(n.operand(), out);
                out.add(n);
            }
            case Expr.Binary b -> {
                collectSubexpressions(b.left(), out);
                collectSubexpressions(b.right(), out);
                out.add(b);
            }
        }
    }
}
