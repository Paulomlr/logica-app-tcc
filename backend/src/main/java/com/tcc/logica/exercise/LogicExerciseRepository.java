package com.tcc.logica.exercise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogicExerciseRepository extends JpaRepository<LogicExercise, Long> {

    List<LogicExercise> findByDifficulty(Difficulty difficulty);

    boolean existsByFormula(String formula);
}
