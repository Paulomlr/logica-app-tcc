package com.tcc.logica.model;

public enum BinaryOperator {
    AND("∧"),
    OR("∨"),
    IMPLIES("→"),
    IFF("↔");

    private final String symbol;

    BinaryOperator(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }
}
