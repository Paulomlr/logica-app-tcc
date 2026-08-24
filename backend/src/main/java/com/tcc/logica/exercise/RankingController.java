package com.tcc.logica.exercise;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
