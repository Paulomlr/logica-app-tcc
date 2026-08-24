package com.tcc.logica.engine;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FormulaParserTest {

    @Test
    void parsesSingleVariable() {
        Expr expr = FormulaParser.parse("p");
        assertThat(expr).isEqualTo(new Expr.Var("p"));
    }

    @Test
    void parsesNegation() {
        Expr expr = FormulaParser.parse("!p");
        assertThat(expr).isEqualTo(new Expr.Not(new Expr.Var("p")));
    }

    @Test
    void acceptsUnicodeOperators() {
        Expr ascii = FormulaParser.parse("p & q");
        Expr unicode = FormulaParser.parse("p ∧ q");
        assertThat(unicode).isEqualTo(ascii);
    }

    @Test
    void respectsPrecedence_andBindsTighterThanOr() {
        // p | q & r  ==  p | (q & r)
        Expr actual = FormulaParser.parse("p | q & r");
        Expr expected = new Expr.Binary(BinaryOperator.OR,
                new Expr.Var("p"),
                new Expr.Binary(BinaryOperator.AND, new Expr.Var("q"), new Expr.Var("r")));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void respectsPrecedence_orBindsTighterThanImplies() {
        // p -> q | r  ==  p -> (q | r)
        Expr actual = FormulaParser.parse("p -> q | r");
        Expr expected = new Expr.Binary(BinaryOperator.IMPLIES,
                new Expr.Var("p"),
                new Expr.Binary(BinaryOperator.OR, new Expr.Var("q"), new Expr.Var("r")));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parenthesesOverridePrecedence() {
        // (p | q) & r
        Expr actual = FormulaParser.parse("(p | q) & r");
        Expr expected = new Expr.Binary(BinaryOperator.AND,
                new Expr.Binary(BinaryOperator.OR, new Expr.Var("p"), new Expr.Var("q")),
                new Expr.Var("r"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void binaryOperatorsAreLeftAssociative() {
        // p -> q -> r  ==  (p -> q) -> r
        Expr actual = FormulaParser.parse("p -> q -> r");
        Expr expected = new Expr.Binary(BinaryOperator.IMPLIES,
                new Expr.Binary(BinaryOperator.IMPLIES, new Expr.Var("p"), new Expr.Var("q")),
                new Expr.Var("r"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void rejectsIncompleteFormula() {
        assertThatThrownBy(() -> FormulaParser.parse("p &"))
                .isInstanceOf(FormulaSyntaxException.class);
    }

    @Test
    void rejectsUnbalancedParentheses() {
        assertThatThrownBy(() -> FormulaParser.parse("(p & q"))
                .isInstanceOf(FormulaSyntaxException.class);
    }

    @Test
    void rejectsUppercaseVariables() {
        assertThatThrownBy(() -> FormulaParser.parse("P & q"))
                .isInstanceOf(FormulaSyntaxException.class);
    }

    @Test
    void rejectsInvalidCharacter() {
        assertThatThrownBy(() -> FormulaParser.parse("p @ q"))
                .isInstanceOf(FormulaSyntaxException.class);
    }

    @Test
    void rejectsFormulaLongerThanMaxLength() {
        String huge = "p" + " & p".repeat(1000);
        assertThat(huge.length()).isGreaterThan(FormulaParser.MAX_FORMULA_LENGTH);

        assertThatThrownBy(() -> FormulaParser.parse(huge))
                .isInstanceOf(FormulaSyntaxException.class);
    }

    @Test
    void acceptsFormulaAtExactlyMaxLength() {
        // "p" repeated with " & " separators, padded to land exactly at the limit
        StringBuilder sb = new StringBuilder("p");
        while (sb.length() + 4 <= FormulaParser.MAX_FORMULA_LENGTH) {
            sb.append(" & p");
        }
        String formula = sb.toString();
        assertThat(formula.length()).isLessThanOrEqualTo(FormulaParser.MAX_FORMULA_LENGTH);

        assertThatCode(() -> FormulaParser.parse(formula)).doesNotThrowAnyException();
    }
}
