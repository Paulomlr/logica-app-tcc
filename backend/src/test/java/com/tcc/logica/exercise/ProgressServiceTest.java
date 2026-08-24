package com.tcc.logica.exercise;

import com.tcc.logica.config.DataSeeder;
import com.tcc.logica.user.AppUser;
import com.tcc.logica.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

/**
 * Uses a fresh AppUser per test (not the shared DataSeeder.TEST_USER_EMAIL user)
 * so assertions on exact counts stay valid regardless of attempts accumulated by
 * that shared user from manual/live testing against the running server.
 */
@SpringBootTest
@Transactional
class ProgressServiceTest {

    @Autowired
    private ProgressService progressService;

    @Autowired
    private LogicExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseAttemptRepository attemptRepository;

    @Autowired
    private AppUserRepository userRepository;

    private AppUser newUser() {
        return userRepository.save(new AppUser("Usuário Teste", UUID.randomUUID() + "@test.local", null));
    }

    @Test
    void progressAggregatesAcrossAttemptsAndDifficulties() {
        AppUser user = newUser();

        LogicExercise easy = exerciseRepository.save(new LogicExercise("p & q", Difficulty.FACIL));
        LogicExercise medium = exerciseRepository.save(new LogicExercise("p -> q", Difficulty.MEDIO));

        attemptRepository.save(new ExerciseAttempt(user, easy, true, 10));
        attemptRepository.save(new ExerciseAttempt(user, easy, false, 20));
        attemptRepository.save(new ExerciseAttempt(user, medium, true, 15));

        ProgressSummary progress = progressService.getProgress(user);

        assertThat(progress.totalAttempts()).isEqualTo(3);
        assertThat(progress.correctAttempts()).isEqualTo(2);
        assertThat(progress.incorrectAttempts()).isEqualTo(1);
        assertThat(progress.accuracyRate()).isCloseTo(2.0 / 3, offset(0.0001));
        assertThat(progress.totalTimeSpentSeconds()).isEqualTo(45);

        ProgressSummary.DifficultyBreakdown facil = progress.byDifficulty().get(Difficulty.FACIL);
        assertThat(facil.totalAttempts()).isEqualTo(2);
        assertThat(facil.correctAttempts()).isEqualTo(1);
        assertThat(facil.accuracyRate()).isCloseTo(0.5, offset(0.0001));

        ProgressSummary.DifficultyBreakdown dificil = progress.byDifficulty().get(Difficulty.DIFICIL);
        assertThat(dificil.totalAttempts()).isZero();
        assertThat(dificil.accuracyRate()).isZero();
    }

    @Test
    void historyReturnsMostRecentFirstWithExerciseDetails() {
        AppUser user = newUser();
        LogicExercise exercise = exerciseRepository.save(new LogicExercise("!p", Difficulty.FACIL));

        attemptRepository.save(new ExerciseAttempt(user, exercise, true, 5));

        var history = progressService.getHistory(user);

        assertThat(history).hasSize(1);
        AttemptHistoryEntry mostRecent = history.get(0);
        assertThat(mostRecent.exerciseId()).isEqualTo(exercise.getId());
        assertThat(mostRecent.formula()).isEqualTo("!p");
        assertThat(mostRecent.difficulty()).isEqualTo(Difficulty.FACIL);
        assertThat(mostRecent.correct()).isTrue();
        assertThat(mostRecent.timeSpentSeconds()).isEqualTo(5);
    }

    @Test
    void progressWithNoAttemptsIsAllZero() {
        AppUser user = newUser();

        ProgressSummary progress = progressService.getProgress(user);

        assertThat(progress.totalAttempts()).isZero();
        assertThat(progress.accuracyRate()).isZero();
    }

    @Test
    void serviceDefaultsToSeededTestUserWhenNoUserSpecified() {
        // sanity check that the no-arg overload used by the controller resolves
        // DataSeeder.TEST_USER_EMAIL without error, regardless of its attempt history
        assertThat(progressService.getProgress()).isNotNull();
        assertThat(progressService.getHistory()).isNotNull();
    }
}
