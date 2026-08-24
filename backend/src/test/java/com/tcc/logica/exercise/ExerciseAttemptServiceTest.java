package com.tcc.logica.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ExerciseAttemptServiceTest {

    @Autowired
    private ExerciseAttemptService service;

    @Autowired
    private LogicExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseAttemptRepository attemptRepository;

    private LogicExercise saveExercise(String formula) {
        return exerciseRepository.save(new LogicExercise(formula, Difficulty.FACIL));
    }

    @Test
    void playViewExposesVariableAssignmentsButNotComputedColumns() {
        LogicExercise exercise = saveExercise("p & q");

        ExercisePlayView view = service.getPlayView(exercise.getId());

        assertThat(view.variables()).containsExactly("p", "q");
        assertThat(view.columnLabels()).containsExactly("p", "q", "(p ∧ q)");
        assertThat(view.columnIsFillable()).containsExactly(false, false, true);
        assertThat(view.rowAssignments()).hasSize(4);
        assertThat(view.rowAssignments().get(0)).containsEntry("p", true).containsEntry("q", true);
    }

    @Test
    void submitAttempt_allCorrect_returnsCorrectTrueAndPersistsAttempt() {
        LogicExercise exercise = saveExercise("p & q");
        long attemptsBefore = attemptRepository.count();

        AttemptSubmissionRequest request = new AttemptSubmissionRequest(
                List.of(List.of(true), List.of(false), List.of(false), List.of(false)), 30);

        AttemptResultResponse result = service.submitAttempt(exercise.getId(), request);

        assertThat(result.correct()).isTrue();
        assertThat(result.correctness()).allSatisfy(row -> assertThat(row).containsOnly(true));
        assertThat(attemptRepository.count()).isEqualTo(attemptsBefore + 1);
    }

    @Test
    void submitAttempt_wrongAnswer_returnsCorrectFalseAndRevealsExpectedValue() {
        LogicExercise exercise = saveExercise("p & q");

        // row 0 (p=T, q=T) should be true; submitting false is wrong
        AttemptSubmissionRequest request = new AttemptSubmissionRequest(
                List.of(List.of(false), List.of(false), List.of(false), List.of(false)), 30);

        AttemptResultResponse result = service.submitAttempt(exercise.getId(), request);

        assertThat(result.correct()).isFalse();
        assertThat(result.correctness().get(0)).containsExactly(false);
        assertThat(result.correctAnswers().get(0)).containsExactly(true);
    }

    @Test
    void submitAttempt_wrongRowCount_returns400() {
        LogicExercise exercise = saveExercise("p & q");

        AttemptSubmissionRequest request = new AttemptSubmissionRequest(List.of(List.of(true)), 10);

        assertThatThrownBy(() -> service.submitAttempt(exercise.getId(), request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400");
    }

    @Test
    void playView_unknownExercise_returns404() {
        assertThatThrownBy(() -> service.getPlayView(-1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }
}
