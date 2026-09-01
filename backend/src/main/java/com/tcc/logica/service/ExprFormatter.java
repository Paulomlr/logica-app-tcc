package com.tcc.logica.service;

import com.tcc.logica.model.Expr;

/**
 * Renders an expression as a display string for table column headers.
 * Every operation (besides a bare variable) is fully parenthesized, so the
 * grouping is always unambiguous to a student reading the table.
 */
public final class ExprFormatter {

    private ExprFormatter() {
    }

    public static String format(Expr expr) {
        return switch (expr) {
            case Expr.Var v -> v.name();
            case Expr.Not n -> "¬" + format(n.operand());
            case Expr.Binary b -> "(" + format(b.left()) + " " + b.operator().symbol() + " " + format(b.right()) + ")";
        };
    }
}
