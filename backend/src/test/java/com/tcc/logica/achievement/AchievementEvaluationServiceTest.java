package com.tcc.logica.achievement;

import com.tcc.logica.exercise.Difficulty;
import com.tcc.logica.exercise.ExerciseAttempt;
import com.tcc.logica.exercise.ExerciseAttemptRepository;
import com.tcc.logica.exercise.LogicExercise;
import com.tcc.logica.exercise.LogicExerciseRepository;
import com.tcc.logica.user.AppUser;
import com.tcc.logica.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AchievementEvaluationServiceTest {

    @Autowired
    private AchievementEvaluationService service;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private LogicExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseAttemptRepository attemptRepository;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    private AppUser newUser() {
        return userRepository.save(new AppUser("Usuário Teste", UUID.randomUUID() + "@test.local", null));
    }

    private LogicExercise exercise(Difficulty difficulty) {
        return exerciseRepository.save(new LogicExercise("p & q", difficulty));
    }

    @Test
    void firstAttemptUnlocksFirstAttemptAchievementOnly() {
        AppUser user = newUser();
        attemptRepository.save(new ExerciseAttempt(user, exercise(Difficulty.FACIL), false, 10));

        List<Achievement> unlocked = service.evaluate(user);

        assertThat(unlocked).extracting(Achievement::getCode).containsExactly(AchievementCodes.FIRST_ATTEMPT);
    }

    @Test
    void correctAttemptUnlocksFirstCorrectTooButOnlyOnce() {
        AppUser user = newUser();
        attemptRepository.save(new ExerciseAttempt(user, exercise(Difficulty.FACIL), true, 10));

        List<Achievement> firstEvaluation = service.evaluate(user);
        assertThat(firstEvaluation).extracting(Achievement::getCode)
                .containsExactlyInAnyOrder(AchievementCodes.FIRST_ATTEMPT, AchievementCodes.FIRST_CORRECT);

        // evaluating again with no new attempts must not re-unlock anything
        List<Achievement> secondEvaluation = service.evaluate(user);
        assertThat(secondEvaluation).isEmpty();
    }

    @Test
    void tenCorrectAnswersUnlocksVeteranAchievement() {
        AppUser user = newUser();
        LogicExercise ex = exercise(Difficulty.FACIL);
        for (int i = 0; i < 10; i++) {
            attemptRepository.save(new ExerciseAttempt(user, ex, true, 5));
        }

        List<Achievement> unlocked = service.evaluate(user);

        assertThat(unlocked).extracting(Achievement::getCode).contains(AchievementCodes.TEN_CORRECT);
    }

    @Test
    void fiveCorrectInARowUnlocksStreakAchievement() {
        AppUser user = newUser();
        LogicExercise ex = exercise(Difficulty.FACIL);
        for (int i = 0; i < 5; i++) {
            attemptRepository.save(new ExerciseAttempt(user, ex, true, 5));
        }

        List<Achievement> unlocked = service.evaluate(user);

        assertThat(unlocked).extracting(Achievement::getCode).contains(AchievementCodes.STREAK_5);
    }

    @Test
    void streakBrokenByWrongAnswerDoesNotUnlock() {
        AppUser user = newUser();
        LogicExercise ex = exercise(Difficulty.FACIL);
        attemptRepository.save(new ExerciseAttempt(user, ex, true, 5));
        attemptRepository.save(new ExerciseAttempt(user, ex, true, 5));
        attemptRepository.save(new ExerciseAttempt(user, ex, false, 5));
        attemptRepository.save(new ExerciseAttempt(user, ex, true, 5));
        attemptRepository.save(new ExerciseAttempt(user, ex, true, 5));

        List<Achievement> unlocked = service.evaluate(user);

        assertThat(unlocked).extracting(Achievement::getCode).doesNotContain(AchievementCodes.STREAK_5);
    }

    @Test
    void correctInEveryDifficultyUnlocksAllLevelsAchievement() {
        AppUser user = newUser();
        attemptRepository.save(new ExerciseAttempt(user, exercise(Difficulty.FACIL), true, 5));
        attemptRepository.save(new ExerciseAttempt(user, exercise(Difficulty.MEDIO), true, 5));
        attemptRepository.save(new ExerciseAttempt(user, exercise(Difficulty.DIFICIL), true, 5));
        attemptRepository.save(new ExerciseAttempt(user, exercise(Difficulty.AVANCADO), true, 5));

        List<Achievement> unlocked = service.evaluate(user);

        assertThat(unlocked).extracting(Achievement::getCode).contains(AchievementCodes.ALL_LEVELS);
        List<UserAchievement> stored = userAchievementRepository.findByUser(user);
        assertThat(stored).hasSameSizeAs(unlocked);
        // must be the real entity (join-fetched), not a lazy Hibernate proxy subclass —
        // a proxy leaks fields like "hibernateLazyInitializer" when serialized to JSON
        assertThat(stored.get(0).getAchievement().getClass()).isEqualTo(Achievement.class);
    }
}
