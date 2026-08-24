package com.tcc.logica.config;

import com.tcc.logica.achievement.Achievement;
import com.tcc.logica.achievement.AchievementCodes;
import com.tcc.logica.achievement.AchievementRepository;
import com.tcc.logica.exercise.Difficulty;
import com.tcc.logica.exercise.LogicExercise;
import com.tcc.logica.exercise.LogicExerciseRepository;
import com.tcc.logica.user.AppUser;
import com.tcc.logica.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds a fixed test user and a starter formula bank so the game flow can be
 * built and exercised end to end before Google OAuth2 login exists.
 * TEST_USER_EMAIL is used as a stand-in for "the current user" until then.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    public static final String TEST_USER_EMAIL = "teste@logica.app";

    private final AppUserRepository appUserRepository;
    private final LogicExerciseRepository logicExerciseRepository;
    private final AchievementRepository achievementRepository;

    public DataSeeder(AppUserRepository appUserRepository,
                       LogicExerciseRepository logicExerciseRepository,
                       AchievementRepository achievementRepository) {
        this.appUserRepository = appUserRepository;
        this.logicExerciseRepository = logicExerciseRepository;
        this.achievementRepository = achievementRepository;
    }

    @Override
    public void run(String... args) {
        seedTestUser();
        seedExercises();
        seedAchievements();
    }

    private void seedTestUser() {
        if (appUserRepository.findByEmail(TEST_USER_EMAIL).isEmpty()) {
            appUserRepository.save(new AppUser("Usuário de Teste", TEST_USER_EMAIL, null));
        }
    }

    private void seedExercises() {
        if (logicExerciseRepository.count() > 0) {
            return;
        }
        List<LogicExercise> exercises = List.of(
                new LogicExercise("p & q", Difficulty.FACIL),
                new LogicExercise("p | q", Difficulty.FACIL),
                new LogicExercise("!p", Difficulty.FACIL),
                new LogicExercise("(p & q) -> r", Difficulty.MEDIO),
                new LogicExercise("p -> (q | r)", Difficulty.MEDIO),
                new LogicExercise("(p | q) & !r", Difficulty.MEDIO),
                new LogicExercise("(p -> q) <-> (!q -> !p)", Difficulty.DIFICIL),
                new LogicExercise("((p & q) -> r) & (r -> !s)", Difficulty.DIFICIL)
        );
        logicExerciseRepository.saveAll(exercises);
    }

    private void seedAchievements() {
        if (achievementRepository.count() > 0) {
            return;
        }
        List<Achievement> achievements = List.of(
                new Achievement(AchievementCodes.FIRST_ATTEMPT, "Primeiro Passo",
                        "Completou seu primeiro exercício."),
                new Achievement(AchievementCodes.FIRST_CORRECT, "Primeira Vitória",
                        "Acertou um exercício pela primeira vez."),
                new Achievement(AchievementCodes.TEN_CORRECT, "Veterano",
                        "Acertou 10 exercícios."),
                new Achievement(AchievementCodes.STREAK_5, "Sequência Perfeita",
                        "Acertou 5 exercícios seguidos."),
                new Achievement(AchievementCodes.ALL_LEVELS, "Completo",
                        "Acertou ao menos um exercício em cada nível de dificuldade.")
        );
        achievementRepository.saveAll(achievements);
    }
}
