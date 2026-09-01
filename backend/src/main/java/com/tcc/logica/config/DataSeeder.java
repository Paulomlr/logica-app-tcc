package com.tcc.logica.config;

import com.tcc.logica.entity.Achievement;
import com.tcc.logica.entity.AppUser;
import com.tcc.logica.model.AchievementCodes;
import com.tcc.logica.model.Difficulty;
import com.tcc.logica.repository.AchievementRepository;
import com.tcc.logica.repository.AppUserRepository;
import com.tcc.logica.repository.LogicExerciseRepository;
import com.tcc.logica.service.ExerciseGenerationService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds a fixed test user, a starter formula bank, and the achievement catalog
 * so the game flow can be built and exercised end to end before Google OAuth2
 * login exists. TEST_USER_EMAIL is used as a stand-in for "the current user"
 * until then.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    public static final String TEST_USER_EMAIL = "teste@logica.app";

    /** Predefined starter bank size per level; POST /exercises/generate adds more on demand. */
    private static final int SEEDED_EXERCISES_PER_DIFFICULTY = 6;

    private final AppUserRepository appUserRepository;
    private final LogicExerciseRepository logicExerciseRepository;
    private final AchievementRepository achievementRepository;
    private final ExerciseGenerationService exerciseGenerationService;

    public DataSeeder(AppUserRepository appUserRepository,
                       LogicExerciseRepository logicExerciseRepository,
                       AchievementRepository achievementRepository,
                       ExerciseGenerationService exerciseGenerationService) {
        this.appUserRepository = appUserRepository;
        this.logicExerciseRepository = logicExerciseRepository;
        this.achievementRepository = achievementRepository;
        this.exerciseGenerationService = exerciseGenerationService;
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
        for (Difficulty difficulty : Difficulty.values()) {
            exerciseGenerationService.generate(difficulty, SEEDED_EXERCISES_PER_DIFFICULTY);
        }
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
