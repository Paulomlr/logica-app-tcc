package com.tcc.logica.exercise;

import com.tcc.logica.achievement.Achievement;

import java.util.List;

public record AttemptResultResponse(
        boolean correct,
        List<List<Boolean>> correctness,
        List<List<Boolean>> correctAnswers,
        List<Achievement> newlyUnlockedAchievements) {
}
