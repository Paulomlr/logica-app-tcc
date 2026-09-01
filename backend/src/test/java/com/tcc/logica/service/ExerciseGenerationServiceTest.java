package com.tcc.logica.service;

import com.tcc.logica.entity.LogicExercise;
import com.tcc.logica.model.Difficulty;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@Transactional
class ExerciseGenerationServiceTest {

    @Autowired
    private ExerciseGenerationService service;

    @Test
    void generatesAndPersistsRequestedCount() {
        List<LogicExercise> created = service.generate(Difficulty.MEDIO, 5);

        assertThat(created).hasSize(5);
        assertThat(created).allSatisfy(exercise -> {
            assertThat(exercise.getId()).isNotNull();
            assertThat(exercise.getDifficulty()).isEqualTo(Difficulty.MEDIO);
            assertThatCode(() -> FormulaParser.parse(exercise.getFormula())).doesNotThrowAnyException();
        });
    }

    @Test
    void generatedFormulasAreDistinct() {
        List<LogicExercise> created = service.generate(Difficulty.DIFICIL, 5);

        long distinctFormulas = created.stream().map(LogicExercise::getFormula).distinct().count();

        assertThat(distinctFormulas).isEqualTo(created.size());
        assertThat(created.stream().map(LogicExercise::getFormula).collect(Collectors.toSet())).hasSize(5);
    }
}
