package com.tcc.logica.dto;

import com.tcc.logica.entity.Achievement;
import java.util.List;

public record AttemptResultResponse(
        boolean correct,
        List<List<Boolean>> correctness,
        List<List<Boolean>> correctAnswers,
        List<Achievement> newlyUnlockedAchievements) {
}
