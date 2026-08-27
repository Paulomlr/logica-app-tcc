package com.tcc.logica.exercise;

import com.tcc.logica.engine.BinaryOperator;
import com.tcc.logica.engine.Expr;
import com.tcc.logica.engine.ExprFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Builds random, syntactically valid formulas whose size (variable pool,
 * operator count, operator variety) scales with {@link Difficulty}. NOT is only
 * ever applied to leaf variables (not to arbitrary subexpressions) to keep
 * generated formulas readable for a teaching tool rather than maximally complex.
 */
public final class FormulaGenerator {

    private static final double NOT_PROBABILITY = 0.3;

    private FormulaGenerator() {
    }

    public static String generate(Difficulty difficulty) {
        return generate(difficulty, new Random());
    }

    static String generate(Difficulty difficulty, Random random) {
        GenerationRules rules = rulesFor(difficulty);
        int operatorCount = rules.minOperators()
                + (rules.maxOperators() > rules.minOperators()
                        ? random.nextInt(rules.maxOperators() - rules.minOperators() + 1)
                        : 0);

        List<Expr> pending = new ArrayList<>();
        for (int i = 0; i <= operatorCount; i++) {
            String varName = rules.variables().get(random.nextInt(rules.variables().size()));
            Expr leaf = new Expr.Var(varName);
            if (random.nextDouble() < NOT_PROBABILITY) {
                leaf = new Expr.Not(leaf);
            }
            pending.add(leaf);
        }

        while (pending.size() > 1) {
            Expr left = pending.remove(random.nextInt(pending.size()));
            Expr right = pending.remove(random.nextInt(pending.size()));
            BinaryOperator operator = rules.operators().get(random.nextInt(rules.operators().size()));
            pending.add(new Expr.Binary(operator, left, right));
        }

        return ExprFormatter.format(pending.get(0));
    }

    private record GenerationRules(List<String> variables, int minOperators, int maxOperators,
                                    List<BinaryOperator> operators) {
    }

    private static GenerationRules rulesFor(Difficulty difficulty) {
        return switch (difficulty) {
            case FACIL -> new GenerationRules(
                    List.of("p", "q"), 1, 1,
                    List.of(BinaryOperator.AND, BinaryOperator.OR));
            case MEDIO -> new GenerationRules(
                    List.of("p", "q", "r"), 2, 3,
                    List.of(BinaryOperator.AND, BinaryOperator.OR, BinaryOperator.IMPLIES));
            case DIFICIL -> new GenerationRules(
                    List.of("p", "q", "r"), 4, 5,
                    List.of(BinaryOperator.AND, BinaryOperator.OR, BinaryOperator.IMPLIES, BinaryOperator.IFF));
            case AVANCADO -> new GenerationRules(
                    List.of("p", "q", "r"), 6, 8,
                    List.of(BinaryOperator.AND, BinaryOperator.OR, BinaryOperator.IMPLIES, BinaryOperator.IFF));
        };
    }
}
