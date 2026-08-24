package com.tcc.logica.exercise;

import com.tcc.logica.achievement.Achievement;
import com.tcc.logica.achievement.UserAchievement;
import com.tcc.logica.achievement.UserAchievementRepository;
import com.tcc.logica.config.DataSeeder;
import com.tcc.logica.user.AppUser;
import com.tcc.logica.user.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Scoped to "the current user" — for now always the seeded test user (see
 * DataSeeder), since Google OAuth2 login doesn't exist yet. Once it does, only
 * currentUser() needs to change (resolve from the security principal instead).
 */
@Service
public class ProgressService {

    private final AppUserRepository userRepository;
    private final ExerciseAttemptRepository attemptRepository;
    private final UserAchievementRepository userAchievementRepository;

    public ProgressService(AppUserRepository userRepository,
                            ExerciseAttemptRepository attemptRepository,
                            UserAchievementRepository userAchievementRepository) {
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.userAchievementRepository = userAchievementRepository;
    }

    public List<AttemptHistoryEntry> getHistory() {
        return getHistory(currentUser());
    }

    public List<AttemptHistoryEntry> getHistory(AppUser user) {
        return attemptRepository.findByUserOrderBySubmittedAtDesc(user).stream()
                .map(a -> new AttemptHistoryEntry(
                        a.getId(),
                        a.getExercise().getId(),
                        a.getExercise().getFormula(),
                        a.getExercise().getDifficulty(),
                        a.isCorrect(),
                        a.getTimeSpentSeconds(),
                        a.getSubmittedAt()))
                .toList();
    }

    public ProgressSummary getProgress() {
        return getProgress(currentUser());
    }

    public ProgressSummary getProgress(AppUser user) {
        List<ExerciseAttempt> attempts = attemptRepository.findByUserOrderBySubmittedAtDesc(user);

        long total = attempts.size();
        long correct = attempts.stream().filter(ExerciseAttempt::isCorrect).count();
        long totalTime = attempts.stream().mapToLong(ExerciseAttempt::getTimeSpentSeconds).sum();

        Map<Difficulty, List<ExerciseAttempt>> byDifficulty = attempts.stream()
                .collect(Collectors.groupingBy(a -> a.getExercise().getDifficulty()));

        Map<Difficulty, ProgressSummary.DifficultyBreakdown> breakdown = new EnumMap<>(Difficulty.class);
        for (Difficulty difficulty : Difficulty.values()) {
            List<ExerciseAttempt> attemptsForLevel = byDifficulty.getOrDefault(difficulty, List.of());
            long levelTotal = attemptsForLevel.size();
            long levelCorrect = attemptsForLevel.stream().filter(ExerciseAttempt::isCorrect).count();
            breakdown.put(difficulty, new ProgressSummary.DifficultyBreakdown(
                    levelTotal, levelCorrect, accuracy(levelCorrect, levelTotal)));
        }

        return new ProgressSummary(total, correct, total - correct, accuracy(correct, total), totalTime, breakdown);
    }

    public List<Achievement> getAchievements() {
        return getAchievements(currentUser());
    }

    public List<Achievement> getAchievements(AppUser user) {
        return userAchievementRepository.findByUser(user).stream()
                .map(UserAchievement::getAchievement)
                .toList();
    }

    private static double accuracy(long correct, long total) {
        return total == 0 ? 0.0 : (double) correct / total;
    }

    private AppUser currentUser() {
        return userRepository.findByEmail(DataSeeder.TEST_USER_EMAIL)
                .orElseThrow(() -> new IllegalStateException("Usuário de teste não encontrado."));
    }
}
