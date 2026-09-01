package com.tcc.logica.service;

import com.tcc.logica.exception.FormulaSyntaxException;
import com.tcc.logica.model.Expr;
import com.tcc.logica.model.TruthTable;
import com.tcc.logica.model.TruthTableRow;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TruthTableGeneratorTest {

    @Test
    void singleVariableHasTwoRows() {
        TruthTable table = TruthTableGenerator.generate(FormulaParser.parse("p"));

        assertThat(table.variables()).containsExactly("p");
        assertThat(table.rows()).hasSize(2);
        assertThat(table.rows().get(0).assignment()).containsEntry("p", true);
        assertThat(table.rows().get(1).assignment()).containsEntry("p", false);
    }

    @Test
    void rowCountIsTwoToThePowerOfVariableCount() {
        TruthTable table = TruthTableGenerator.generate(FormulaParser.parse("(p & q) -> r"));

        assertThat(table.variables()).containsExactly("p", "q", "r");
        assertThat(table.rows()).hasSize(8);
    }

    @Test
    void includesOneColumnPerSubexpression_endingWithFullFormula() {
        TruthTable table = TruthTableGenerator.generate(FormulaParser.parse("p & !q"));

        // columns: p, q, !q, (p & !q)
        assertThat(table.columnLabels()).containsExactly("p", "q", "¬q", "(p ∧ ¬q)");
    }

    @Test
    void resultColumnMatchesDirectEvaluation() {
        Expr formula = FormulaParser.parse("(p & q) | r");
        TruthTable table = TruthTableGenerator.generate(formula);

        for (TruthTableRow row : table.rows()) {
            boolean expected = FormulaEvaluator.evaluate(formula, row.assignment());
            assertThat(row.result()).isEqualTo(expected);
        }
    }

    @Test
    void deduplicatesRepeatedSubexpressions() {
        // "p" appears twice but should only produce one "p" column
        TruthTable table = TruthTableGenerator.generate(FormulaParser.parse("p & p"));

        assertThat(table.columnLabels()).containsExactly("p", "(p ∧ p)");
    }

    @Test
    void rejectsMoreThanSixVariables() {
        Expr formula = FormulaParser.parse("a & b & c & d & e & f & g");

        assertThatThrownBy(() -> TruthTableGenerator.generate(formula))
                .isInstanceOf(FormulaSyntaxException.class);
    }
}
