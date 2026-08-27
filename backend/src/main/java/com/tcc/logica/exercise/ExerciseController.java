package com.tcc.logica.exercise;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private static final int MAX_GENERATE_COUNT = 20;
    private static final Random RANDOM = new Random();

    private final LogicExerciseRepository exerciseRepository;
    private final ExerciseAttemptService attemptService;
    private final ExerciseGenerationService generationService;

    public ExerciseController(LogicExerciseRepository exerciseRepository,
                               ExerciseAttemptService attemptService,
                               ExerciseGenerationService generationService) {
        this.exerciseRepository = exerciseRepository;
        this.attemptService = attemptService;
        this.generationService = generationService;
    }

    @GetMapping
    public List<LogicExercise> list(@RequestParam(required = false) Difficulty difficulty) {
        return difficulty != null ? exerciseRepository.findByDifficulty(difficulty) : exerciseRepository.findAll();
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LogicExercise> generate(@RequestParam(required = false) Difficulty difficulty,
                                         @RequestParam(defaultValue = "1") int count) {
        if (count < 1 || count > MAX_GENERATE_COUNT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "count deve estar entre 1 e " + MAX_GENERATE_COUNT + ".");
        }
        // No difficulty given -> "surprise challenge": pick one at random per exercise,
        // instead of generating `count` exercises all at the same random difficulty.
        if (difficulty == null) {
            List<LogicExercise> created = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                created.addAll(generationService.generate(randomDifficulty(), 1));
            }
            return created;
        }
        return generationService.generate(difficulty, count);
    }

    private Difficulty randomDifficulty() {
        Difficulty[] values = Difficulty.values();
        return values[RANDOM.nextInt(values.length)];
    }

    @GetMapping("/{id}/play")
    public ExercisePlayView play(@PathVariable Long id) {
        return attemptService.getPlayView(id);
    }

    @PostMapping("/{id}/attempts")
    public AttemptResultResponse submit(@PathVariable Long id, @Valid @RequestBody AttemptSubmissionRequest request) {
        return attemptService.submitAttempt(id, request);
    }
}
