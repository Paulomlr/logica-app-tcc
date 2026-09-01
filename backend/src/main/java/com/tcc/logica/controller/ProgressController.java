package com.tcc.logica.controller;

import com.tcc.logica.dto.AttemptHistoryEntry;
import com.tcc.logica.dto.ProgressSummary;
import com.tcc.logica.entity.Achievement;
import com.tcc.logica.service.ProgressService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
