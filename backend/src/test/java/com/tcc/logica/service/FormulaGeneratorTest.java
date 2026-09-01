package com.tcc.logica.service;

import com.tcc.logica.model.Difficulty;
import com.tcc.logica.model.Expr;
import java.util.Random;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.assertj.core.api.Assertions.assertThat;

class FormulaGeneratorTest {

    @ParameterizedTest
    @EnumSource(Difficulty.class)
    void generatesParseableFormulasForEveryDifficulty(Difficulty difficulty) {
        Random random = new Random(42);
        for (int i = 0; i < 50; i++) {
            String formula = FormulaGenerator.generate(difficulty, random);

            Expr expr = FormulaParser.parse(formula);
            // must be evaluable end to end
            assertThat(TruthTableGenerator.generate(expr).rows()).isNotEmpty();
        }
    }

    @ParameterizedTest
    @EnumSource(Difficulty.class)
    void variableCountStaysWithinExpectedPoolForDifficulty(Difficulty difficulty) {
        Random random = new Random(7);
        int maxAllowedVariables = switch (difficulty) {
            case FACIL -> 2;
            case MEDIO, DIFICIL, AVANCADO -> 3;
        };

        for (int i = 0; i < 50; i++) {
            String formula = FormulaGenerator.generate(difficulty, random);
            Expr expr = FormulaParser.parse(formula);
            int variableCount = TruthTableGenerator.generate(expr).variables().size();

            assertThat(variableCount).isLessThanOrEqualTo(maxAllowedVariables);
        }
    }

    @ParameterizedTest
    @EnumSource(Difficulty.class)
    void isDeterministicForAGivenSeed(Difficulty difficulty) {
        String first = FormulaGenerator.generate(difficulty, new Random(123));
        String second = FormulaGenerator.generate(difficulty, new Random(123));

        assertThat(first).isEqualTo(second);
    }
}
