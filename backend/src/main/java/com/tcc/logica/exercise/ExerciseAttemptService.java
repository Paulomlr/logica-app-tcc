package com.tcc.logica.exercise;

import com.tcc.logica.achievement.Achievement;
import com.tcc.logica.achievement.AchievementEvaluationService;
import com.tcc.logica.config.DataSeeder;
import com.tcc.logica.engine.Expr;
import com.tcc.logica.engine.FormulaParser;
import com.tcc.logica.engine.TruthTable;
import com.tcc.logica.engine.TruthTableGenerator;
import com.tcc.logica.engine.TruthTableRow;
import com.tcc.logica.user.AppUser;
import com.tcc.logica.user.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ExerciseAttemptService {

    private final LogicExerciseRepository exerciseRepository;
    private final AppUserRepository userRepository;
    private final ExerciseAttemptRepository attemptRepository;
    private final AchievementEvaluationService achievementEvaluationService;

    public ExerciseAttemptService(LogicExerciseRepository exerciseRepository,
                                   AppUserRepository userRepository,
                                   ExerciseAttemptRepository attemptRepository,
                                   AchievementEvaluationService achievementEvaluationService) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
        this.achievementEvaluationService = achievementEvaluationService;
    }

    public ExercisePlayView getPlayView(Long exerciseId) {
        LogicExercise exercise = findExercise(exerciseId);
        TruthTable table = generateTable(exercise);

        List<java.util.Map<String, Boolean>> rowAssignments = table.rows().stream()
                .map(TruthTableRow::assignment)
                .toList();
        List<Boolean> columnIsFillable = table.columnLabels().stream()
                .map(label -> !table.variables().contains(label))
                .toList();

        return new ExercisePlayView(exercise.getId(), exercise.getFormula(), table.variables(),
                table.columnLabels(), columnIsFillable, rowAssignments);
    }

    public AttemptResultResponse submitAttempt(Long exerciseId, AttemptSubmissionRequest request) {
        LogicExercise exercise = findExercise(exerciseId);
        TruthTable table = generateTable(exercise);
        List<Integer> fillableColumnIndexes = fillableColumnIndexes(table);

        if (request.answers().size() != table.rows().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Esperado " + table.rows().size() + " linha(s), recebido " + request.answers().size() + ".");
        }

        List<List<Boolean>> correctness = new ArrayList<>();
        List<List<Boolean>> correctAnswers = new ArrayList<>();
        boolean allCorrect = true;

        for (int r = 0; r < table.rows().size(); r++) {
            List<Boolean> submittedRow = request.answers().get(r);
            if (submittedRow.size() != fillableColumnIndexes.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Linha " + r + ": esperado " + fillableColumnIndexes.size()
                                + " valor(es), recebido " + submittedRow.size() + ".");
            }

            List<Boolean> rowCorrectness = new ArrayList<>();
            List<Boolean> rowCorrectAnswers = new ArrayList<>();
            TruthTableRow tableRow = table.rows().get(r);

            for (int c = 0; c < fillableColumnIndexes.size(); c++) {
                boolean expected = tableRow.columnValues().get(fillableColumnIndexes.get(c));
                boolean submitted = Boolean.TRUE.equals(submittedRow.get(c));
                boolean isCorrect = expected == submitted;
                rowCorrectness.add(isCorrect);
                rowCorrectAnswers.add(expected);
                if (!isCorrect) {
                    allCorrect = false;
                }
            }
            correctness.add(rowCorrectness);
            correctAnswers.add(rowCorrectAnswers);
        }

        AppUser testUser = userRepository.findByEmail(DataSeeder.TEST_USER_EMAIL)
                .orElseThrow(() -> new IllegalStateException("Usuário de teste não encontrado."));
        attemptRepository.save(new ExerciseAttempt(testUser, exercise, allCorrect, request.timeSpentSeconds()));

        List<Achievement> newlyUnlocked = achievementEvaluationService.evaluate(testUser);

        return new AttemptResultResponse(allCorrect, correctness, correctAnswers, newlyUnlocked);
    }

    private LogicExercise findExercise(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercício não encontrado: " + id));
    }

    private TruthTable generateTable(LogicExercise exercise) {
        Expr formula = FormulaParser.parse(exercise.getFormula());
        return TruthTableGenerator.generate(formula);
    }

    private List<Integer> fillableColumnIndexes(TruthTable table) {
        return IntStream.range(0, table.columnLabels().size())
                .filter(i -> !table.variables().contains(table.columnLabels().get(i)))
                .boxed()
                .toList();
    }
}
