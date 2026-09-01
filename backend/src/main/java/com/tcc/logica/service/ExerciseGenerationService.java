package com.tcc.logica.service;

import com.tcc.logica.entity.LogicExercise;
import com.tcc.logica.model.Difficulty;
import com.tcc.logica.repository.LogicExerciseRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExerciseGenerationService {

    private static final int MAX_ATTEMPTS_PER_FORMULA = 10;

    private final LogicExerciseRepository exerciseRepository;

    public ExerciseGenerationService(LogicExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<LogicExercise> generate(Difficulty difficulty, int count) {
        List<LogicExercise> created = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String formula = nextUniqueFormula(difficulty);
            created.add(exerciseRepository.save(new LogicExercise(formula, difficulty)));
        }
        return created;
    }

    private String nextUniqueFormula(Difficulty difficulty) {
        String formula = FormulaGenerator.generate(difficulty);
        for (int attempt = 1; attempt < MAX_ATTEMPTS_PER_FORMULA && exerciseRepository.existsByFormula(formula); attempt++) {
            formula = FormulaGenerator.generate(difficulty);
        }
        return formula;
    }
}
