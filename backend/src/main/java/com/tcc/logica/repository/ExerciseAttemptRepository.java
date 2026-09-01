package com.tcc.logica.repository;

import com.tcc.logica.dto.RankingEntry;
import com.tcc.logica.entity.AppUser;
import com.tcc.logica.entity.ExerciseAttempt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseAttemptRepository extends JpaRepository<ExerciseAttempt, Long> {

    @Query("""
            select a from ExerciseAttempt a
            join fetch a.exercise
            where a.user = :user
            order by a.submittedAt desc
            """)
    List<ExerciseAttempt> findByUserOrderBySubmittedAtDesc(@Param("user") AppUser user);

    long countByUserAndCorrectTrue(AppUser user);

    @Query("""
            select new com.tcc.logica.dto.RankingEntry(a.user.id, a.user.name, count(a))
            from ExerciseAttempt a
            where a.correct = true
            group by a.user.id, a.user.name
            order by count(a) desc
            """)
    List<RankingEntry> findRanking();
}
