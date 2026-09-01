package com.tcc.logica.service;

import com.tcc.logica.model.Expr;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FormulaEvaluatorTest {

    @Test
    void evaluatesAnd() {
        Expr expr = FormulaParser.parse("p & q");
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", true))).isTrue();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", false))).isFalse();
    }

    @Test
    void evaluatesOr() {
        Expr expr = FormulaParser.parse("p | q");
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", false, "q", false))).isFalse();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", false))).isTrue();
    }

    @Test
    void evaluatesNot() {
        Expr expr = FormulaParser.parse("!p");
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true))).isFalse();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", false))).isTrue();
    }

    @Test
    void evaluatesImplies_falseOnlyWhenAntecedentTrueAndConsequentFalse() {
        Expr expr = FormulaParser.parse("p -> q");
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", false))).isFalse();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", false, "q", false))).isTrue();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", false, "q", true))).isTrue();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", true))).isTrue();
    }

    @Test
    void evaluatesIff_trueWhenBothSidesMatch() {
        Expr expr = FormulaParser.parse("p <-> q");
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", true))).isTrue();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", false, "q", false))).isTrue();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", false))).isFalse();
    }

    @Test
    void evaluatesNestedFormula() {
        // (p & !q) -> r
        Expr expr = FormulaParser.parse("(p & !q) -> r");
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", false, "r", false))).isFalse();
        assertThat(FormulaEvaluator.evaluate(expr, Map.of("p", true, "q", true, "r", false))).isTrue();
    }
}
