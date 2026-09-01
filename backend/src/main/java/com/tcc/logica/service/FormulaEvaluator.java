package com.tcc.logica.service;

import com.tcc.logica.model.Expr;
import java.util.Map;

public final class FormulaEvaluator {

    private FormulaEvaluator() {
    }

    public static boolean evaluate(Expr expr, Map<String, Boolean> assignment) {
        return switch (expr) {
            case Expr.Var v -> {
                Boolean value = assignment.get(v.name());
                if (value == null) {
                    throw new IllegalArgumentException("Valor não atribuído para a variável: " + v.name());
                }
                yield value;
            }
            case Expr.Not n -> !evaluate(n.operand(), assignment);
            case Expr.Binary b -> {
                boolean left = evaluate(b.left(), assignment);
                boolean right = evaluate(b.right(), assignment);
                yield switch (b.operator()) {
                    case AND -> left && right;
                    case OR -> left || right;
                    case IMPLIES -> !left || right;
                    case IFF -> left == right;
                };
            }
        };
    }
}
