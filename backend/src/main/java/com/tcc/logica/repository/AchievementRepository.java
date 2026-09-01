package com.tcc.logica.repository;

import com.tcc.logica.entity.Achievement;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Optional<Achievement> findByCode(String code);
}
