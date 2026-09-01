package com.tcc.logica.repository;

import com.tcc.logica.entity.LogicExercise;
import com.tcc.logica.model.Difficulty;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogicExerciseRepository extends JpaRepository<LogicExercise, Long> {

    List<LogicExercise> findByDifficulty(Difficulty difficulty);

    boolean existsByFormula(String formula);
}
