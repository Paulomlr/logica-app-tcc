package com.tcc.logica.model;

public sealed interface Expr permits Expr.Var, Expr.Not, Expr.Binary {

    record Var(String name) implements Expr {
    }

    record Not(Expr operand) implements Expr {
    }

    record Binary(BinaryOperator operator, Expr left, Expr right) implements Expr {
    }
}
