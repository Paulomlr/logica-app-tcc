package com.tcc.logica.exercise;

import com.tcc.logica.achievement.Achievement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/me")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/history")
    public List<AttemptHistoryEntry> history() {
        return progressService.getHistory();
    }

    @GetMapping("/progress")
    public ProgressSummary progress() {
        return progressService.getProgress();
    }

    @GetMapping("/achievements")
    public List<Achievement> achievements() {
        return progressService.getAchievements();
    }
}
