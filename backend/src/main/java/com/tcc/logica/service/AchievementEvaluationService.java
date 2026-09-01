package com.tcc.logica.service;

import com.tcc.logica.entity.Achievement;
import com.tcc.logica.entity.AppUser;
import com.tcc.logica.entity.ExerciseAttempt;
import com.tcc.logica.entity.UserAchievement;
import com.tcc.logica.model.AchievementCodes;
import com.tcc.logica.model.Difficulty;
import com.tcc.logica.repository.AchievementRepository;
import com.tcc.logica.repository.ExerciseAttemptRepository;
import com.tcc.logica.repository.UserAchievementRepository;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Call {@link #evaluate(AppUser)} after persisting a new ExerciseAttempt.
 * Checks every achievement's condition against the user's full attempt
 * history and unlocks (persists a UserAchievement for) any that are newly met.
 */
@Service
public class AchievementEvaluationService {

    private static final int STREAK_LENGTH = 5;
    private static final long CORRECT_COUNT_FOR_VETERAN = 10;

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final ExerciseAttemptRepository attemptRepository;

    public AchievementEvaluationService(AchievementRepository achievementRepository,
                                         UserAchievementRepository userAchievementRepository,
                                         ExerciseAttemptRepository attemptRepository) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.attemptRepository = attemptRepository;
    }

    public List<Achievement> evaluate(AppUser user) {
        List<ExerciseAttempt> attemptsDesc = attemptRepository.findByUserOrderBySubmittedAtDesc(user);
        long correctCount = attemptsDesc.stream().filter(ExerciseAttempt::isCorrect).count();

        List<Achievement> newlyUnlocked = new ArrayList<>();
        unlockIf(user, AchievementCodes.FIRST_ATTEMPT, !attemptsDesc.isEmpty(), newlyUnlocked);
        unlockIf(user, AchievementCodes.FIRST_CORRECT, correctCount > 0, newlyUnlocked);
        unlockIf(user, AchievementCodes.TEN_CORRECT, correctCount >= CORRECT_COUNT_FOR_VETERAN, newlyUnlocked);
        unlockIf(user, AchievementCodes.STREAK_5, hasStreak(attemptsDesc, STREAK_LENGTH), newlyUnlocked);
        unlockIf(user, AchievementCodes.ALL_LEVELS, hasCorrectInAllLevels(attemptsDesc), newlyUnlocked);
        return newlyUnlocked;
    }

    private void unlockIf(AppUser user, String code, boolean conditionMet, List<Achievement> newlyUnlocked) {
        if (!conditionMet) {
            return;
        }
        Achievement achievement = achievementRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Conquista não cadastrada: " + code));
        if (userAchievementRepository.existsByUserAndAchievement(user, achievement)) {
            return;
        }
        userAchievementRepository.save(new UserAchievement(user, achievement));
        newlyUnlocked.add(achievement);
    }

    private boolean hasStreak(List<ExerciseAttempt> attemptsDesc, int streakLength) {
        if (attemptsDesc.size() < streakLength) {
            return false;
        }
        return attemptsDesc.subList(0, streakLength).stream().allMatch(ExerciseAttempt::isCorrect);
    }

    private boolean hasCorrectInAllLevels(List<ExerciseAttempt> attemptsDesc) {
        Set<Difficulty> levelsWithCorrectAnswer = attemptsDesc.stream()
                .filter(ExerciseAttempt::isCorrect)
                .map(a -> a.getExercise().getDifficulty())
                .collect(Collectors.toSet());
        return levelsWithCorrectAnswer.containsAll(EnumSet.allOf(Difficulty.class));
    }
}
