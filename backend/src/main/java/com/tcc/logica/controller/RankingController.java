package com.tcc.logica.controller;

import com.tcc.logica.dto.RankingEntry;
import com.tcc.logica.repository.ExerciseAttemptRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final ExerciseAttemptRepository attemptRepository;

    public RankingController(ExerciseAttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    @GetMapping
    public List<RankingEntry> ranking() {
        return attemptRepository.findRanking();
    }
}
